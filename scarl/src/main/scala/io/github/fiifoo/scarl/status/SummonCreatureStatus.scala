package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoices
import io.github.fiifoo.scarl.effect.TickEffect
import io.github.fiifoo.scarl.effect.area.{ConsumeChargeEffect, SummonCreatureEffect}

case class SummonCreatureStatus(id: ActiveStatusId,
                                tick: Tick,
                                target: ContainerId,
                                summon: WeightedChoices[CreatureKindId],
                                summonDescription: Option[String] = None,
                                interval: Tick,
                                charge: Option[Charge] = None
                               ) extends ActiveStatus with ChargeableStatus {
  def setTick(tick: Tick): SummonCreatureStatus = copy(tick = tick)

  def setCharge(charge: Option[Charge]): SummonCreatureStatus = {
    this.copy(charge = charge)
  }

  def apply(s: State): List[Effect] = {
    val location = target(s).location

    val effects = if (Selectors.getLocationEntities(s)(location) exists {
      case _: CreatureId => true
      case _ => false
    }) {
      List(TickEffect(id, interval))
    } else {
      List(
        TickEffect(id, interval),
        SummonCreatureEffect(summon, location, summonDescription)
      )
    }

    this.charge.map(charge => {
      effects ::: List(ConsumeChargeEffect(this.id, charge, location))
    }) getOrElse {
      effects
    }
  }
}
