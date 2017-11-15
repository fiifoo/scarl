package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.ai.tactic.MissileTactic
import io.github.fiifoo.scarl.core.Selectors.{getCreatureStats, getLocationEntities}
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.Line

case class ShootMissileEffect(attacker: CreatureId,
                              targetLocation: Location,
                              sourceLocation: Location,
                              kind: CreatureKindId,
                              parent: Option[Effect] = None
                             ) extends Effect {

  def apply(s: State): EffectResult = {
    val range = getCreatureStats(s)(attacker).missileLauncher.range
    val from = attacker(s).location
    val destination = (Line(from, targetLocation) take range + 1).last
    val behavior = MissileTactic(
      destination = destination,
      target = kind(s).missile flatMap (missile => if (missile.guidance.isEmpty) None else getLocationEntities(s)(destination) collectFirst {
        case c: CreatureId => SafeCreatureId(c)
      })
    )

    val result = kind(s).copy(
      behavior = behavior,
      faction = attacker(s).faction,
    ).toLocation(
      s = s,
      idSeq = s.idSeq,
      location = from,
      owner = Some(SafeCreatureId(attacker))
    )

    result.entity.missile map (_ => EffectResult(result.mutations)) getOrElse EffectResult()
  }
}
