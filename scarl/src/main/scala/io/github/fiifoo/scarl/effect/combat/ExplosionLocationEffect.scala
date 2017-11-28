package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, EntityId, LocatableId}
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.core.{Location, Selectors, State}
import io.github.fiifoo.scarl.effect.area.RemoveEntityEffect
import io.github.fiifoo.scarl.geometry.Obstacle
import io.github.fiifoo.scarl.rule.AttackRule
import io.github.fiifoo.scarl.rule.AttackRule.{Attacker, Defender}

import scala.util.Random

case class ExplosionLocationEffect(source: LocatableId,
                                   location: Location,
                                   stats: Explosive,
                                   parent: Option[Effect] = None
                                  ) extends Effect {

  def apply(s: State): EffectResult = {
    val (random, rng) = s.rng()
    val effects = (targets(s) map attack(s, random)).toList
    val destroyEffect = Obstacle.explosion(s)(location) map destroy(s)

    EffectResult(
      RngMutation(rng),
      destroyEffect map (_ :: effects) getOrElse effects,
    )
  }

  def targets(s: State): Set[CreatureId] = {
    (Selectors.getLocationEntities(s)(location) - source) collect {
      case c: CreatureId => c
    }
  }

  private def attack(s: State, random: Random)(target: CreatureId): Effect = {
    val targetStats = getCreatureStats(s)(target)

    val result = AttackRule(random)(
      Attacker(stats.attack, stats.damage),
      Defender(targetStats.defence, targetStats.armor)
    )

    if (result.hit) {
      ExplosionHitEffect(source, target, result, location, Some(this))
    } else {
      ExplosionMissEffect(source, target, location, Some(this))
    }
  }

  private def destroy(state: State)(target: EntityId): Effect = {
    RemoveEntityEffect(
      target = target,
      location = Some(location),
      description = None,
      parent = Some(this),
    )
  }
}
