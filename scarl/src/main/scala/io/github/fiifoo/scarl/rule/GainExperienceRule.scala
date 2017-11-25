package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId}
import io.github.fiifoo.scarl.effect.combat.{DeathEffect, ExplosionHitEffect, HitEffect, TrapHitEffect}

object GainExperienceRule {
  val default = 1

  def apply(s: State, effect: DeathEffect): Option[(CreatureId, Int)] = {
    getAttacker(s, effect) map (attacker => {
      (attacker, default)
    })
  }

  private def getAttacker(s: State, effect: Effect): Option[CreatureId] = {
    effect.parent.collect({
      case hit: ExplosionHitEffect => hit.source match {
        case creature: CreatureId => Some(getOwner(s, creature))
        case container: ContainerId => getOwner(s, container)
        case _ => None
      }
      case hit: HitEffect => Some(getOwner(s, hit.attacker))
      case hit: TrapHitEffect => getOwner(s, hit.trap)
      case other: Effect => getAttacker(s, other)
    }).flatten
  }

  private def getOwner(s: State, creature: CreatureId): CreatureId = {
    creature(s).owner flatMap (_ (s) map (owner => getOwner(s, owner.id))) getOrElse creature
  }

  private def getOwner(s: State, container: ContainerId): Option[CreatureId] = {
    container(s).owner flatMap (_ (s) map (owner => getOwner(s, owner.id)))
  }
}
