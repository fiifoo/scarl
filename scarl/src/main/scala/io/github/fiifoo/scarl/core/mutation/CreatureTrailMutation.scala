package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location

case class CreatureTrailMutation(creature: CreatureId, location: Location) extends Mutation {

  val max = 5

  def apply(s: State): State = {
    val previous = s.creature.trails.getOrElse(creature, List())
    val next = location :: (if (previous.size >= max) previous.init else previous)

    s.copy(creature = s.creature.copy(
      trails = s.creature.trails + (creature -> next)
    ))
  }
}
