package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{CreatureId, FactionId}

case class FactionMemberIndexRemoveMutation(member: CreatureId, faction: FactionId) {
  type Index = Map[FactionId, Set[CreatureId]]

  def apply(index: Index): Index = {
    val members = index(faction) - member

    if (members.isEmpty) {
      index - faction
    } else {
      index + (faction -> members)
    }
  }
}
