package fi.fiifoo.scarl.core.test_assets

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.CreatureId
import fi.fiifoo.scarl.core.mutation.CreatureDamageMutation

case class TestDamageEffect(target: CreatureId, amount: Int) extends Effect {

  def apply(s: State): EffectResult = {
    val mutation = CreatureDamageMutation(target, target(s).damage + amount)

    EffectResult(mutation)
  }
}
