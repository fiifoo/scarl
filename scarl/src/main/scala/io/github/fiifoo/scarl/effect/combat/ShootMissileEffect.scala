package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.ai.tactic.MissileTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.{getCreatureStats, getLocationEntities}
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry.{Line, Location}
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.kind.Kind.Options

case class ShootMissileEffect(attacker: CreatureId,
                              targetLocation: Location,
                              sourceLocation: Location,
                              kind: CreatureKindId,
                              parent: Option[Effect] = None
                             ) extends Effect {

  def apply(s: State): EffectResult = {
    val attackerStats = getCreatureStats(s)(attacker)
    val range = attackerStats.launcher.range
    val from = attacker(s).location
    val destination = (Line(from, targetLocation) take range + 1).last
    val behavior = MissileTactic(
      destination = destination,
      target = kind(s).traits.missile flatMap (missile => if (missile.guidance.isEmpty) None else getLocationEntities(s)(destination) collectFirst {
        case c: CreatureId => SafeCreatureId(c)
      })
    )

    val result = kind(s)
      .copy(
        behavior = behavior,
        stats = getMissileStats(s, attackerStats)
      ).apply(s, s.idSeq, from, Options(owner = Some(attacker)))

    result.entity.traits.missile map (_ => EffectResult(result.mutations)) getOrElse EffectResult()
  }

  private def getMissileStats(s: State, attackerStats: Stats): Stats = {
    val stats = kind(s).stats

    stats.copy(
      explosive = stats.explosive.add(attackerStats.explosive)
    )
  }
}
