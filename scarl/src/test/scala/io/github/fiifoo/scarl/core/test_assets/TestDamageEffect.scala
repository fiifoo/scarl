package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.CreatureDamageMutation

case class TestDamageEffect(target: CreatureId,
                            amount: Int,
                            parent: Option[Effect] = None
                           ) extends Effect {

  def apply(s: State): EffectResult = {
    val mutation = CreatureDamageMutation(target, target(s).damage + amount)

    EffectResult(mutation)
  }
}
