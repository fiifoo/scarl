package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Selectors.isVisibleItem
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId, ItemPower}
import io.github.fiifoo.scarl.effect.combat.TrapAttackEffect

case class TrapAttackPower(attack: Int,
                           damage: Int,
                           hitDescription: Option[String] = None,
                           deflectDescription: Option[String] = None,
                           missDescription: Option[String] = None
                          ) extends ItemPower {

  def apply(s: State, item: ItemId, user: Option[CreatureId] = None): List[Effect] = {
    val container = item(s).container match {
      case container: ContainerId => Some(container)
      case _ => None
    }

    val effect = container flatMap (container => {
      user map (user => {
        val evade = isVisibleItem(s, user)(item)

        TrapAttackEffect(
          container,
          user,
          attack,
          damage,
          evade,
          hitDescription,
          deflectDescription,
          missDescription
        )
      })
    })

    effect map (List(_)) getOrElse List()
  }
}
