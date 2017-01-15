package fi.fiifoo.scarl.core.test_assets

import fi.fiifoo.scarl.core.action.Action
import fi.fiifoo.scarl.core.effect.Effect
import fi.fiifoo.scarl.core.entity.Creature
import fi.fiifoo.scarl.core.{Location, State}

case class TestMoveAction(location: Location) extends Action {
  val cost = 100

  def apply(s: State, actor: Creature): List[Effect] = {
    List(
      TestTickEffect(actor.id, cost),
      TestMoveEffect(actor.id, location)
    )
  }
}
