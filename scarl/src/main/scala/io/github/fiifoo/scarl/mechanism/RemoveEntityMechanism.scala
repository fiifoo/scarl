package io.github.fiifoo.scarl.mechanism

import io.github.fiifoo.scarl.core.effect.{Effect, RemoveEntityEffect}
import io.github.fiifoo.scarl.core.entity.Selectors.getLocationEntities
import io.github.fiifoo.scarl.core.entity.{Machinery, Taggable}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Mechanism
import io.github.fiifoo.scarl.core.{State, Tag}

case class RemoveEntityMechanism(description: Option[String],
                                 disposable: Boolean,
                                 tag: Tag,
                                 removeDescription: Option[String],
                                ) extends Mechanism {
  def interact(s: State, machinery: Machinery, control: Location): List[Effect] = {
    val entities = machinery.targets flatMap getLocationEntities(s) map (_ (s)) collect {
      case entity: Taggable if entity.tags.contains(tag) => entity
    }

    val effects = (entities map (entity => {
      RemoveEntityEffect(entity.id, Some(entity.location), removeDescription)
    })).toList

    activate(machinery, effects)
  }
}
