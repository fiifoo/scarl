package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.Selectors.hasKey
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.item.PrivateKey
import io.github.fiifoo.scarl.core.mutation.ItemLockMutation
import io.github.fiifoo.scarl.effect.creature.ReceiveKeyEffect

case class LockItemEffect(target: ItemId,
                          locker: CreatureId,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    val key = PrivateKey(locker)

    val receiveKey = if (!hasKey(s)(locker)(key)) {
      Some(ReceiveKeyEffect(locker, key))
    } else {
      None
    }

    EffectResult(
      ItemLockMutation(target, Some(key)),
      List(receiveKey).flatten
    )
  }
}
