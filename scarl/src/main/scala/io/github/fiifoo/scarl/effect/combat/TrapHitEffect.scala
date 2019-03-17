package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.creature.Condition
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.rule.AttackRule

case class TrapHitEffect(trap: ContainerId,
                         target: CreatureId,
                         result: AttackRule.Result,
                         conditions: List[Condition],
                         location: Location,
                         description: Option[String] = None,
                         deflectDescription: Option[String] = None,
                         parent: Option[Effect] = None
                        ) extends Effect with AbstractHitEffect
