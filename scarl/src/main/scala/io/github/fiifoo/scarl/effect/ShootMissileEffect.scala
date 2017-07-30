package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.ai.tactic.MissileTactic
import io.github.fiifoo.scarl.core.Selectors.{getCreatureStats, getLocationEntities}
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.mutation.{NewEntityMutation, TacticMutation}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.Line

case class ShootMissileEffect(attacker: CreatureId,
                              location: Location,
                              kind: CreatureKindId,
                              parent: Option[Effect] = None
                             ) extends Effect {

  def apply(s: State): EffectResult = {
    val range = getCreatureStats(s)(attacker).missileLauncher.range
    val from = attacker(s).location
    val destination = (Line(from, location) take range + 1).last
    val creature = kind(s)(s, from).copy(
      faction = attacker(s).faction,
      owner = Some(SafeCreatureId(attacker))
    )

    creature.missile map (missile => {
      val target = if (missile.guidance.isEmpty) None else getLocationEntities(s)(destination) collectFirst {
        case c: CreatureId => c
      }

      val tactic = MissileTactic(
        actor = creature.id,
        destination = destination,
        target = target map SafeCreatureId.apply
      )

      EffectResult(List(
        NewEntityMutation(creature),
        TacticMutation(tactic)
      ))
    }) getOrElse EffectResult()
  }
}
