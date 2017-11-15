package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId}
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}

case class SleepEffect(target: CreatureId,
                       parent: Option[Effect] = None
                      ) extends Effect {

  def apply(s: State): EffectResult = {
    val (nextId, nextIdSeq) = s.idSeq()

    val status = SleepStatus(ActiveStatusId(nextId), s.tick, target)

    EffectResult(List(
      IdSeqMutation(nextIdSeq),
      NewEntityMutation(status)),
    )
  }
}
