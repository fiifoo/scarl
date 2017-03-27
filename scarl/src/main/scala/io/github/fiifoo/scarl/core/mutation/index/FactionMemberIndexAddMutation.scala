package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{CreatureId, FactionId}

case class FactionMemberIndexAddMutation(member: CreatureId, faction: FactionId) {
  type Index = Map[FactionId, Set[CreatureId]]

  def apply(index: Index): Index = {
    val members = if (index.isDefinedAt(faction)) {
      index(faction) + member
    } else {
      Set(member)
    }

    index + (faction -> members)
  }
}
