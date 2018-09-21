package io.github.fiifoo.scarl.mechanism

import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Selectors.getLocationEntities
import io.github.fiifoo.scarl.core.entity.{Machinery, Taggable}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Mechanism
import io.github.fiifoo.scarl.core.kind.KindId
import io.github.fiifoo.scarl.core.{State, Tag}
import io.github.fiifoo.scarl.effect.area.TransformEffect

case class TransformEntityMechanism(description: Option[String],
                                    disposable: Boolean,
                                    tag: Tag,
                                    transformTo: KindId,
                                    transformDescription: Option[String],
                                   ) extends Mechanism {
  def interact(s: State, machinery: Machinery, control: Location): List[Effect] = {
    val entities = machinery.targets flatMap getLocationEntities(s) map (_ (s)) collect {
      case entity: Taggable if entity.tags.contains(tag) => entity
    }

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
