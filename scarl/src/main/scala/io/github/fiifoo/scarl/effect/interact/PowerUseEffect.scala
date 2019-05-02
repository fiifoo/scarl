package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, Power, UsableId}
import io.github.fiifoo.scarl.effect.creature.{ChangeResourcesEffect, ShortageEffect}

case class PowerUseEffect(user: Option[CreatureId],
                          target: UsableId,
                          power: Power,
                          requireResources: Boolean,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    user map (user => {
      shortage(s, user) getOrElse {
        val usedEffect = PowerUsedEffect(user, this.power.description, Some(this))
        val effects = this.power(s, target, Some(user))

        this.power.resources map (resources => {
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
    }) getOrElse {
      EffectResult(this.power(s, this.target, None))
    }
  }

  private def shortage(s: State, user: CreatureId): Option[EffectResult] = {
    if (!this.requireResources) {
      return None
    }

    this.power.resources flatMap (resources => {
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
