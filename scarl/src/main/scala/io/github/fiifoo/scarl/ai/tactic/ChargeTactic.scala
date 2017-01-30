package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.{AttackAction, MoveAction}
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.{Line, Los}

case class ChargeTactic(actor: CreatureId, target: SafeCreatureId, destination: Location) extends Tactic {

  private def _range = 5 // should come from creature

  def apply(s: State): (Tactic, Action) = {
    target(s) map (target => {
      val line = Line(actor(s).location, target.location)

      if (line.size <= _range + 1 && Los(s)(line)) {

        val charge = copy(destination = line.last)
        val action = if (line.size <= 2) {
          AttackAction(target.id)
        } else {
          MoveAction(line(1))
        }

        (charge, action)

      } else {

        val pursue = PursueTactic(actor, SafeCreatureId(target.id), destination)
        pursue(s)

      }

    }) getOrElse RoamTactic(actor)(s)
  }
}
