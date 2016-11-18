package fi.fiifoo.scarl.status

import fi.fiifoo.scarl.core.Selectors.getLocationEntities
import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.Effect
import fi.fiifoo.scarl.core.entity._
import fi.fiifoo.scarl.effect.{DamageEffect, RemoveStatusEffect, TickEffect}

case class BurnStatus(id: ActiveStatusId,
                      tick: Int,
                      target: LocatableId,
                      expires: Option[Int] = None
                     ) extends ActiveStatus {
  val interval = 100
  val damage = 1

  def setTick(tick: Int): Actor = copy(tick = tick)

  def activate(s: State): List[Effect] = {
    val location = target(s).location
    val entities = getLocationEntities(location)(s)
    val targets = entities collect { case creature: Creature => creature.id }

    val effects = targets map (target => DamageEffect(target, damage))

    if (expires.forall(_ > tick)) {
      TickEffect(id, interval) :: effects
    } else {
      RemoveStatusEffect(id) :: effects
    }
  }
}
