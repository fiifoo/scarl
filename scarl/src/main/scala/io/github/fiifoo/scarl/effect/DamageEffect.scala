package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.CreatureDamageMutation

case class DamageEffect(target: CreatureId,
                        amount: Int,
                        parent: Option[Effect] = None
                       ) extends Effect {

  def apply(s: State): EffectResult = {
    val creature = target(s)
    val damage = creature.damage + amount
    val health = creature.stats.health

    val mutation = CreatureDamageMutation(target, damage)

    if (creature.damage < health && damage >= health) {
      EffectResult(mutation, DeathEffect(target, Some(this)))
    } else {
      EffectResult(mutation)
    }
  }
}
