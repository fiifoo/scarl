package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.creature.Party
import io.github.fiifoo.scarl.core.entity.CreatureId

case class PartyMemberIndexAddMutation(member: CreatureId, party: Party) {
  type Index = Map[Party, Set[CreatureId]]

  def apply(index: Index): Index = {
    val members = if (index.isDefinedAt(party)) {
      index(party) + member
    } else {
      Set(member)
    }

    index + (party -> members)
  }
}
