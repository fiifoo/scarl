package io.github.fiifoo.scarl.mechanism

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Machinery
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Mechanism
import io.github.fiifoo.scarl.core.kind.KindId
import io.github.fiifoo.scarl.effect.area.TransformEffect

case class TransformEntityMechanism(transformTo: KindId, transformDescription: Option[String]) extends Mechanism {
  def interact(s: State, machinery: Machinery, control: Location): List[Effect] = {
    val entities = machinery.getTargetEntities(s)

    val effects = (entities map (entity => {
      TransformEffect(
        from = entity.id,
        to = transformTo,
        description = transformDescription
      )
    })).toList

    activate(machinery, effects)
  }
}
