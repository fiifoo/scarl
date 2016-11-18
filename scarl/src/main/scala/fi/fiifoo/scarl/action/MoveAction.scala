package fi.fiifoo.scarl.action

import fi.fiifoo.scarl.core.action.Action
import fi.fiifoo.scarl.core.effect.Effect
import fi.fiifoo.scarl.core.entity.Creature
import fi.fiifoo.scarl.core.{Location, State}
import fi.fiifoo.scarl.effect.{MoveEffect, TickEffect}

case class MoveAction(location: Location) extends Action {
  val cost = 100

  def apply(s: State, actor: Creature): List[Effect] = {
    List(
      TickEffect(actor.id, cost),
      MoveEffect(actor.id, location)
    )
  }
}
