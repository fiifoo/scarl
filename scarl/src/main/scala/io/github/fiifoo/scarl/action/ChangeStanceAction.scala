package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.creature.Stance
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.creature.ChangeStanceEffect

case class ChangeStanceAction(stance: Option[Stance]) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    List(
      // no tick here!
      ChangeStanceEffect(actor, this.stance, continuous = true)
    )
  }
}
