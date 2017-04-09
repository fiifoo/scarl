package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.core.entity.{CreatureId, EntityId}
import io.github.fiifoo.scarl.core.{Location, State}

object ValidatorUtils {
  def adjacentLocation(s: State, actor: CreatureId, location: Location): Boolean = {
    val actorLocation = actor(s).location

    actorLocation != location &&
      Math.abs(actorLocation.x - location.x) <= 1 &&
      Math.abs(actorLocation.y - location.y) <= 1
  }

  def entityExists(s: State, entity: EntityId): Boolean = {
    s.entities.isDefinedAt(entity)
  }
}
