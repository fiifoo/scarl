package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Condition
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId}
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.rule.AttackRule.{Attacker, Defender}
import io.github.fiifoo.scarl.rule.TrapAttackRule

case class TrapAttackEffect(trap: ContainerId,
                            target: CreatureId,
                            attack: Int,
                            damage: Int,
                            conditions: List[Condition],
                            evade: Boolean,
                            hitDescription: Option[String] = None,
                            deflectDescription: Option[String] = None,
                            missDescription: Option[String] = None,
                            parent: Option[Effect] = None
                           ) extends Effect {

  def apply(s: State): EffectResult = {
    val (random, rng) = s.rng()
    val defenderStats = getCreatureStats(s)(target)

    val result = TrapAttackRule(random)(
      Attacker(attack, damage),
      Defender(defenderStats.defence, defenderStats.armor),
      evade
    )

    val effect = if (result.hit) {
      TrapHitEffect(trap, target, result, conditions, target(s).location, hitDescription, deflectDescription, Some(this))
    } else {
      TrapMissEffect(trap, target, target(s).location, missDescription, Some(this))
    }

    EffectResult(
      RngMutation(rng),
      effect
    )
  }
}
