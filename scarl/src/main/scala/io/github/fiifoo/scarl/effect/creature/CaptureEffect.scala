package io.github.fiifoo.scarl.effect.creature

import io.github.fiifoo.scarl.ai.tactic.FollowOwnerTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation._

case class CaptureEffect(target: CreatureId,
                         capturer: CreatureId,
                         location: Location,
                         parent: Option[Effect] = None
                        ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(List(
      CreatureFactionMutation(target, capturer(s).faction),
      CreatureOwnerMutation(target, Some(SafeCreatureId(capturer))),
      CreatureBehaviorMutation(target, FollowOwnerTactic),
      CreatureTacticMutation(target, None),
      CreatureUsableMutation(target, None)
    ))
  }
}
