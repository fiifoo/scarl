package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.{AttackAction, MoveAction, PassAction}
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.{Location, Rng, State}
import io.github.fiifoo.scarl.geometry.{Line, Los, Path}

case class ChargeTactic(actor: CreatureId, target: SafeCreatureId, destination: Location) extends Tactic {

  def apply(s: State, rng: Rng): (Tactic, Action, Rng) = {
    target(s) map (target => {
      val location = actor(s).location
      val line = Line(location, target.location)
      val range = actor(s).stats.sight.range

      if (line.size <= range + 1 && Los(s)(line)) {

        val charge = copy(destination = target.location)
        val action = if (line.size <= 2) {
          AttackAction(target.id)
        } else {
          Path(s)(location, target.location) map (path => MoveAction(path.head)) getOrElse PassAction()
        }

        (charge, action, rng)

      } else {

        val pursue = PursueTactic(actor, SafeCreatureId(target.id), destination)
        pursue(s, rng)

      }

    }) getOrElse RoamTactic(actor)(s, rng)
  }
}
