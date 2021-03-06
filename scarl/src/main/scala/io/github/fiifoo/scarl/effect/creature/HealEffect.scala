package io.github.fiifoo.scarl.effect.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.CreatureDamageMutation

case class HealEffect(target: CreatureId,
                      amount: Int,
                      location: Location,
                      parent: Option[Effect] = None
                     ) extends Effect {

  def apply(s: State): EffectResult = {
    val creature = target(s)
    val damage = if (creature.damage > amount) creature.damage - amount else 0

    EffectResult(CreatureDamageMutation(target, damage))
  }
}
