package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.kind.KindId
import io.github.fiifoo.scarl.core.power.{CreaturePower, CreaturePowerId}
import io.github.fiifoo.scarl.effect.area.TransformEffect

case class TransformCreaturePower(id: CreaturePowerId,
                                  to: KindId,
                                  description: Option[String] = None
                                 ) extends CreaturePower {

  def apply(s: State, creature: CreatureId, user: Option[CreatureId] = None): List[Effect] = {
    List(TransformEffect(
      from = creature,
      to = to,
      owner = user,
      description = description
    ))
  }
}
