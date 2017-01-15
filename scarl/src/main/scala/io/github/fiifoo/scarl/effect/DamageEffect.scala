package fi.fiifoo.scarl.effect

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.CreatureId
import fi.fiifoo.scarl.core.mutation.CreatureDamageMutation

case class DamageEffect(target: CreatureId, amount: Int) extends Effect {

  def apply(s: State): EffectResult = {
    val creature = target(s)
    val damage = creature.damage + amount

    val mutation = CreatureDamageMutation(target, damage)

    if (creature.damage < creature.health && damage >= creature.health) {
      EffectResult(mutation, DeathEffect(target))
    } else {
      EffectResult(mutation)
    }
  }
}
