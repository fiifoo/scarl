package io.github.fiifoo.scarl.effect.creature.condition

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Condition
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId, Selectors}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation._
import io.github.fiifoo.scarl.status.CreatureConditionStatus

case class GainConditionEffect(target: CreatureId,
                               condition: Condition,
                               strength: Int,
                               location: Location,
                               parent: Option[Effect] = None
                              ) extends Effect {

  def apply(s: State): EffectResult = {
    val (nextId, idSeq) = s.idSeq.apply()
    val existing = Selectors.getCreatureConditionStatuses(s)(this.target) collectFirst {
      case status if status.condition.key == condition.key => status
    }

    val statusMutation = existing map (existing => {
      if (existing.strength < this.strength) {
        Some(ConditionStrengthMutation(existing, this.strength))
      } else {
        None
      }
    }) getOrElse {
      val status = CreatureConditionStatus(ActiveStatusId(nextId), s.tick, s.tick, this.target, this.condition, this.strength)

      Some(NewEntityMutation(status))
    }

    EffectResult(List(
      Some(IdSeqMutation(idSeq)),
      statusMutation
    ).flatten)
  }
}
