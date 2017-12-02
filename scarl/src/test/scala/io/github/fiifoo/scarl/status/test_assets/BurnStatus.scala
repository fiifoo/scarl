package io.github.fiifoo.scarl.status.test_assets

import io.github.fiifoo.scarl.core.Selectors.getLocationEntities
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.area.RemoveEntityEffect
import io.github.fiifoo.scarl.effect.combat.DamageEffect

case class BurnStatus(id: ActiveStatusId,
                      tick: Tick,
                      target: LocatableId,
                      expires: Option[Tick] = None
                     ) extends ActiveStatus {
  val interval = 100
  val damage = 1

  def setTick(tick: Tick): BurnStatus = copy(tick = tick)

  def apply(s: State): List[Effect] = {
    val location = target(s).location
    val effects = getLocationEntities(s)(location).toList collect {
      case target: CreatureId => DamageEffect(target, damage)
    }

    if (expires.forall(_ > tick)) {
      TickEffect(id, interval) :: effects
    } else {
      RemoveEntityEffect(id) :: effects
    }
  }
}
