package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, Power, UsableId}
import io.github.fiifoo.scarl.effect.creature.{ChangeResourcesEffect, ShortageEffect}

case class PowerUseEffect(user: CreatureId,
                          target: UsableId,
                          power: Power,
                          requireResources: Boolean,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    shortage(s) getOrElse {
      val usedEffect = PowerUsedEffect(user, power.description, Some(this))
      val effects = power(s, target, Some(user))

      power.resources map (resources => {
        val resourceEffect = ChangeResourcesEffect(
          user,
          resources.health,
          resources.energy,
          resources.materials,
          resources.components,
          Some(this)
        )

        EffectResult(usedEffect :: resourceEffect :: effects)
      }) getOrElse {
        EffectResult(usedEffect :: effects)
      }
    }
  }

  private def shortage(s: State): Option[EffectResult] = {
    if (!requireResources) {
      return None
    }

    power.resources flatMap (resources => {
      val creature = user(s)

      if (-resources.energy > creature.resources.energy ||
        -resources.materials > creature.resources.materials ||
        -resources.components > creature.resources.components
      ) {
        Some(EffectResult(
          ShortageEffect(
            creature.id,
            -resources.energy > creature.resources.energy,
            -resources.materials > creature.resources.materials,
            -resources.components > creature.resources.components
          )
        ))
      } else {
        None
      }
    })
  }
}
