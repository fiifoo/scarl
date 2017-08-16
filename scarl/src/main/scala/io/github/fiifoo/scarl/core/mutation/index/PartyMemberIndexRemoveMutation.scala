package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.creature.Party
import io.github.fiifoo.scarl.core.entity.CreatureId

case class PartyMemberIndexRemoveMutation(member: CreatureId, party: Party) {
  type Index = Map[Party, Set[CreatureId]]

  def apply(index: Index): Index = {
    val members = index(party) - member

    if (members.isEmpty) {
      index - party
    } else {
      index + (party -> members)
    }
  }
}
