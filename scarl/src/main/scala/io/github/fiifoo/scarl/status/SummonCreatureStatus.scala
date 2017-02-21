package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.effect.{SummonCreatureEffect, TickEffect}

case class SummonCreatureStatus(id: ActiveStatusId,
                                tick: Int,
                                target: ContainerId,
                                summon: CreatureKindId,
                                interval: Int
                               ) extends ActiveStatus {
  def setTick(tick: Int): Actor = copy(tick = tick)

  def apply(s: State): List[Effect] = {
    val location = target(s).location
    val creature = summon(s)(s, location)

    List(
      TickEffect(id, interval),
      SummonCreatureEffect(creature, target)
    )
  }
}
