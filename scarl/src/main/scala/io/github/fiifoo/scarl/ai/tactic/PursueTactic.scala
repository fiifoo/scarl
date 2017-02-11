package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.MoveAction
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.{Location, Rng, State}
import io.github.fiifoo.scarl.geometry.{Line, Los}

case class PursueTactic(actor: CreatureId, target: SafeCreatureId, destination: Location) extends Tactic {

  private def _range = 5 // should come from creature

  def apply(s: State, rng: Rng): (Tactic, Action, Rng) = {
    target(s) flatMap (target => {
      val location = actor(s).location
      val line = Line(location, target.location)

      if (line.size <= _range + 1 && Los(s)(line)) {

        val charge = ChargeTactic(actor, SafeCreatureId(target.id), line.last)
        Some(charge(s, rng))

      } else if (location != destination) {

        val move = MoveAction(Line(location, destination)(1))
        Some((this, move, rng))

      } else {
        None
      }

    }) getOrElse RoamTactic(actor)(s, rng)
  }
}
