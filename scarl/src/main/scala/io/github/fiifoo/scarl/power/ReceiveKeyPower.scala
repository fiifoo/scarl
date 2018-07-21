package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId, ItemPower}
import io.github.fiifoo.scarl.effect.creature.ReceiveKeyEffect

case object ReceiveKeyPower extends ItemPower {

  def apply(s: State, item: ItemId, user: Option[CreatureId] = None): List[Effect] = {
    val effect = item(s).key flatMap (key => {
      user map (user => {
        ReceiveKeyEffect(user, key)
      })
    })

    effect map (List(_)) getOrElse List()
  }
}
