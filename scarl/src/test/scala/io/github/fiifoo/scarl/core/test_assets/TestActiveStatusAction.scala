package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId

case class TestActiveStatusAction() extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    List(
      TestTickEffect(actor),
      TestActorStatusEffect(actor)
    )
  }
}
