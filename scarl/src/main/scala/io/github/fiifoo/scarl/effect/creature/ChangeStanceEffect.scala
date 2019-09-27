package io.github.fiifoo.scarl.effect.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stance
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStanceStatuses
import io.github.fiifoo.scarl.core.entity.{CreatureId, PassiveStatusId}
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation, RemovableEntityMutation, StanceActiveMutation}
import io.github.fiifoo.scarl.status.CreatureStanceStatus

case class ChangeStanceEffect(target: CreatureId,
                              stance: Option[Stance],
                              continuous: Boolean = false,
                              parent: Option[Effect] = None
                             ) extends Effect {

  def apply(s: State): EffectResult = {
    val (nextId, nextIdSeq) = s.idSeq()

    val (deactivate, remove) = if (this.continuous) {
      (Set(), getCreatureStanceStatuses(s)(this.target))
    } else {
      getCreatureStanceStatuses(s)(this.target) partition (_.continuous)
    }

    val addMutation = this.stance map (stance => {
      NewEntityMutation(CreatureStanceStatus(
        PassiveStatusId(nextId),
        this.target,
        stance,
        this.continuous,
        Some(stance.duration)
      ))
    })

    val deactivateMutations = deactivate.toList map (x => StanceActiveMutation(x, active = false))
    val removeMutations = remove.toList map (x => RemovableEntityMutation(x.id))

    EffectResult(
      deactivateMutations ::: removeMutations ::: List(
        Some(IdSeqMutation(nextIdSeq)),
        addMutation,
      ).flatten
    )
  }
}
