package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, EntityId}
import io.github.fiifoo.scarl.core.geometry.Location

object ValidatorUtils {
  def isAdjacentLocation(s: State, actor: CreatureId)(location: Location): Boolean = {
    val actorLocation = actor(s).location

    actorLocation != location &&
      Math.abs(actorLocation.x - location.x) <= 1 &&
      Math.abs(actorLocation.y - location.y) <= 1
  }

  def isAdjacentOrCurrentLocation(s: State, actor: CreatureId)(location: Location): Boolean = {
    location == actor(s).location || isAdjacentLocation(s, actor)(location)
  }

  def isEnemy(s: State, actor: CreatureId, creature: CreatureId): Boolean = {
    actor(s).faction(s).enemies.contains(creature(s).faction)
  }

  def entityExists(s: State)(entity: EntityId): Boolean = {
    s.entities.isDefinedAt(entity)
  }
}
