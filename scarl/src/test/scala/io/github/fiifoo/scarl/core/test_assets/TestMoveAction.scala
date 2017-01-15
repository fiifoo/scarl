package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.core.{Location, State}

case class TestMoveAction(location: Location) extends Action {
  val cost = 100

  def apply(s: State, actor: Creature): List[Effect] = {
    List(
      TestTickEffect(actor.id, cost),
      TestMoveEffect(actor.id, location)
    )
  }
}
