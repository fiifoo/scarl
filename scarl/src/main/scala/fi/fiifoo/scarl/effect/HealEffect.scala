package fi.fiifoo.scarl.effect

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.CreatureId
import fi.fiifoo.scarl.core.mutation.CreatureDamageMutation

case class HealEffect(target: CreatureId, amount: Int) extends Effect {

  def apply(s: State): EffectResult = {
    val creature = target(s)
    val damage = if (creature.damage > amount) creature.damage - amount else 0

    EffectResult(CreatureDamageMutation(target, damage))
  }
}
