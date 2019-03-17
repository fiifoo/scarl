package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.creature.Condition
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{CreatureId, LocatableId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.rule.AttackRule

case class ExplosionHitEffect(source: LocatableId,
                              target: CreatureId,
                              result: AttackRule.Result,
                              conditions: List[Condition],
                              location: Location,
                              parent: Option[Effect] = None
                             ) extends Effect with AbstractHitEffect
