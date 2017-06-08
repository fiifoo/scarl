package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Character
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CreatureCharacterMutation(creature: CreatureId, character: Character) extends Mutation {

  def apply(s: State): State = {
    val mutated = creature(s).copy(character = Some(character))

    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
