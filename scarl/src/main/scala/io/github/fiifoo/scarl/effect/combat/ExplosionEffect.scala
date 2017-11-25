package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, LocatableId}
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.core.{Location, Selectors, State}
import io.github.fiifoo.scarl.rule.AttackRule
import io.github.fiifoo.scarl.rule.AttackRule.{Attacker, Defender}

import scala.util.Random

case class ExplosionEffect(source: LocatableId,
                           location: Location,
                           stats: Explosive,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    val (random, rng) = s.rng()

    EffectResult(
      RngMutation(rng),
      (targets(s) map attack(s, random)).toList
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
}
