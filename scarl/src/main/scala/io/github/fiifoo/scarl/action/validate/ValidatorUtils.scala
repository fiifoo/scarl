package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stance
import io.github.fiifoo.scarl.core.entity.Selectors.{getCreatureHostileFactions, getCreatureStanceStatuses, getCreatureStats}
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
    val enemies = getCreatureHostileFactions(s)(actor)

    enemies.contains(creature(s).faction)
  }

  def isValidStanceAttack(s: State, actor: CreatureId)(stance: Stance): Boolean = {
    !(getCreatureStanceStatuses(s)(actor) exists (status => {
      (stance.key != status.stance.key) &&
        (status.duration exists (x => x > 0 && x != status.stance.duration))
    }))
  }

  def isValidStanceChange(s: State, actor: CreatureId)(stance: Option[Stance]): Boolean = {
    (stance forall getCreatureStats(s)(actor).stances.contains) &&
      !(getCreatureStanceStatuses(s)(actor) exists (status => {
        status.duration exists (x => x > 0 && x != status.stance.duration)
      }))
  }

  def entityExists(s: State)(entity: EntityId): Boolean = {
    s.entities.isDefinedAt(entity)
  }
}
