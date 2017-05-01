package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.Rng.WeightedChoices
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.{Selectors, State}
import io.github.fiifoo.scarl.effect.{SummonSomeCreatureEffect, TickEffect}

case class SummonCreatureStatus(id: ActiveStatusId,
                                tick: Int,
                                target: ContainerId,
                                summon: WeightedChoices[CreatureKindId],
                                interval: Int
                               ) extends ActiveStatus {
  def setTick(tick: Int): SummonCreatureStatus = copy(tick = tick)

  def apply(s: State): List[Effect] = {
    val location = target(s).location

    if (Selectors.getLocationEntities(s)(location) exists {
      case _: CreatureId => true
      case _ => false
    }) {
      List(TickEffect(id, interval))
    } else {
      List(
        TickEffect(id, interval),
        SummonSomeCreatureEffect(summon, location, target)
      )
    }
  }
}
