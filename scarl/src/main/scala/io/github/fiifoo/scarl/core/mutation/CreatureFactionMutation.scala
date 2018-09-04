package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.index.{FactionMemberIndexAddMutation, FactionMemberIndexRemoveMutation}

case class CreatureFactionMutation(creature: CreatureId, faction: FactionId) extends Mutation {

  def apply(s: State): State = {
    val current = creature(s)
    val mutated = current.copy(faction = faction)

    s.copy(
      entities = s.entities + (mutated.id -> mutated),
      index = s.index.copy(
        factionMembers = FactionMemberIndexAddMutation(creature, mutated.faction)(
          FactionMemberIndexRemoveMutation(creature, current.faction)(
            s.index.factionMembers
          )
        )
      )
    )
  }
}
