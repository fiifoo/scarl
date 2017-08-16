package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Party
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.mutation.index.{PartyMemberIndexAddMutation, PartyMemberIndexRemoveMutation}

case class CreaturePartyMutation(creature: CreatureId, party: Party) extends Mutation {

  def apply(s: State): State = {
    val previous = creature(s)
    val next = previous.copy(party = party)

    s.copy(
      entities = s.entities + (creature -> next),
      index = mutateIndex(s.index, previous, next)
    )
  }

  private def mutateIndex(index: State.Index, previous: Creature, next: Creature): State.Index = {
    val remove = PartyMemberIndexRemoveMutation(creature, previous.party)
    val add = PartyMemberIndexAddMutation(creature, next.party)

    index.copy(partyMembers = add(remove(index.partyMembers)))
  }

}
