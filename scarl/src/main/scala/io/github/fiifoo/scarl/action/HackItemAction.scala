package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.effect.TickEffect
import io.github.fiifoo.scarl.effect.interact.HackEffect

case class HackItemAction(target: ItemId) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    List(
      TickEffect(actor),
      HackEffect(actor, target, actor(s).location)
    )
  }
}
