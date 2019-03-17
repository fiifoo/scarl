package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.creature.Condition
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.TickEffect
import io.github.fiifoo.scarl.effect.creature.condition.ApplyConditionEffect

case class CreatureConditionStatus(id: ActiveStatusId,
                                   tick: Tick,
                                   createdAt: Tick,
                                   target: CreatureId,
                                   condition: Condition,
                                   strength: Int,
                                  ) extends ActiveStatus with ConditionStatus {
  def setTick(tick: Tick): CreatureConditionStatus = this.copy(tick = tick)

  def setStrength(strength: Tick): ConditionStatus = this.copy(strength = strength)

  def apply(s: State): List[Effect] = {
    List(
      TickEffect(this.id),
      ApplyConditionEffect(this, this.target, this.tick - this.createdAt, this.target(s).location)
    )
  }
}
