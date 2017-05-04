package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.Selectors.{getCreatureStats, getLocationEntities}
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, EntityId, WallId}
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.Line
import io.github.fiifoo.scarl.rule.AttackRule

case class ShootEffect(attacker: CreatureId,
                       location: Location,
                       parent: Option[Effect] = None
                      ) extends Effect {

  def apply(s: State): EffectResult = {
    val path = Line(attacker(s).location, location).tail take range(s)
    val target = (path flatMap locationTarget(s) _).headOption

    target collect {
      case target: CreatureId => attackResult(s, target)
      case wall: WallId => badShotResult(Some(wall))
    } getOrElse {
      badShotResult()
    }
  }

  private def attackResult(s: State, target: CreatureId): EffectResult = {
    val (result, rng) = AttackRule.ranged(s, attacker, target)

    val effect = if (result.hit) {
      HitEffect(attacker, target, result, Some(this))
    } else {
      MissEffect(attacker, target, Some(this))
    }

    EffectResult(
      RngMutation(rng),
      effect
    )
  }

  private def badShotResult(wall: Option[WallId] = None): EffectResult = {
    EffectResult(BadShotEffect(attacker, wall))
  }

  private def range(s: State): Int = {
    getCreatureStats(s)(attacker).ranged.range
  }

  private def locationTarget(s: State)(location: Location): Option[EntityId] = {
    getLocationEntities(s)(location) collectFirst {
      case creature: CreatureId => creature
      case wall: WallId => wall
    }
  }
}
