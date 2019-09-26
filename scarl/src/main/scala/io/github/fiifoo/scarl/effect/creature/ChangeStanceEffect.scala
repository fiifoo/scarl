package io.github.fiifoo.scarl.effect.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stance
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStanceStatuses
import io.github.fiifoo.scarl.core.entity.{CreatureId, PassiveStatusId}
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.status.CreatureStanceStatus

case class ChangeStanceEffect(target: CreatureId,
                              stance: Stance,
                              continuous: Boolean = false,
                              parent: Option[Effect] = None
                             ) extends Effect {

  def apply(s: State): EffectResult = {
    val (nextId, nextIdSeq) = s.idSeq()

    val status = CreatureStanceStatus(
      PassiveStatusId(nextId),
      this.target,
      this.stance,
      this.continuous,
      Some(this.stance.duration)
    )

    val removeExisting = getCreatureStanceStatuses(s)(this.target).toList map (x => RemovableEntityMutation(x.id))

    EffectResult(
      removeExisting ::: List(
        IdSeqMutation(nextIdSeq),
        NewEntityMutation(status),
      )
    )
  }
}
