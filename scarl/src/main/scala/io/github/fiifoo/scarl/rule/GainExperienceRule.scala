package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.combat.{DeathEffect, HitEffect}

object GainExperienceRule {
  val default = 1

  def apply(s: State, effect: DeathEffect): Option[(CreatureId, Int)] = {
    getAttacker(effect) map (attacker => {
      (getOwner(s, attacker), default)
    })
  }

  private def getAttacker(effect: Effect): Option[CreatureId] = {
    effect.parent.collect({
      case hit: HitEffect => Some(hit.attacker)
      case other: Effect => getAttacker(other)
    }).flatten
  }

  private def getOwner(s: State, creature: CreatureId): CreatureId = {
    creature(s).owner flatMap (_ (s) map (owner => getOwner(s, owner.id))) getOrElse creature
  }
}
