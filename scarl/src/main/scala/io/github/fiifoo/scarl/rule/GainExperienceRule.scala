package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.{DeathEffect, HitEffect}

object GainExperienceRule {
  val default = 1

  def apply(s: State, effect: DeathEffect): Option[(CreatureId, Int)] = {
    attacker(effect) map (attacker => {
      (attacker, default)
    })
  }

  private def attacker(effect: Effect): Option[CreatureId] = {
    effect.parent.collect({
      case hit: HitEffect => Some(hit.attacker)
      case other: Effect => attacker(other)
    }).flatten
  }
}
