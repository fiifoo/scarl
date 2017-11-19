package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId}
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.rule.AttackRule
import io.github.fiifoo.scarl.rule.AttackRule.{Attacker, Defender}

case class TrapAttackEffect(trap: ContainerId,
                            target: CreatureId,
                            attack: Int,
                            damage: Int,
                            hitDescription: Option[String] = None,
                            deflectDescription: Option[String] = None,
                            missDescription: Option[String] = None,
                            parent: Option[Effect] = None
                           ) extends Effect {

  def apply(s: State): EffectResult = {
    val (random, rng) = s.rng()
    val defenderStats = getCreatureStats(s)(target)

    val result = AttackRule(random)(
      Attacker(attack, damage),
      Defender(defenderStats.defence, defenderStats.armor)
    )

    val effect = if (result.hit) {
      TrapHitEffect(trap, target, result, target(s).location, hitDescription, deflectDescription, Some(this))
    } else {
      TrapMissEffect(trap, target, target(s).location, missDescription, Some(this))
    }

    EffectResult(
      RngMutation(rng),
      effect
    )
  }
}
