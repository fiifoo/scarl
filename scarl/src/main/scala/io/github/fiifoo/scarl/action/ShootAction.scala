package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.item.Equipment.RangedSlot
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.{ShootEffect, ShootMissileEffect, TickEffect}

case class ShootAction(location: Location) extends Action {
  val cost = 100

  def apply(s: State, actor: CreatureId): List[Effect] = {
    val effect = getEquippedMissile(s, actor) map (missile => {
      ShootMissileEffect(actor, location, missile)
    }) getOrElse {
      ShootEffect(actor, location)
    }

    List(
      TickEffect(actor, cost),
      effect
    )
  }

  private def getEquippedMissile(s: State, actor: CreatureId): Option[CreatureKindId] = {
    s.equipments.get(actor) flatMap (_.get(RangedSlot) flatMap (_ (s).rangedWeapon flatMap (_.missile)))
  }
}
