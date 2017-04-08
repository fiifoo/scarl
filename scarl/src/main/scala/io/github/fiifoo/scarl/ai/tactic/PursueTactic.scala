package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.MoveAction
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.{Location, Rng, State}
import io.github.fiifoo.scarl.geometry.{Line, Los, Path}

case class PursueTactic(actor: CreatureId, target: SafeCreatureId, destination: Location) extends Tactic {

  def apply(s: State, rng: Rng): (Tactic, Action, Rng) = {
    target(s) flatMap (target => {
      val location = actor(s).location
      val line = Line(location, target.location)
      val range = actor(s).stats.sight.range

      if (line.size <= range + 1 && Los(s)(line)) {

        val charge = ChargeTactic(actor, SafeCreatureId(target.id), target.location)
        Some(charge(s, rng))

      } else if (location != destination) {

        val moveOption = Path(s)(location, destination) map (path => MoveAction(path.head))
        moveOption map ((this, _, rng))

      } else {
        None
      }

    }) getOrElse RoamTactic(actor)(s, rng)
  }
}
