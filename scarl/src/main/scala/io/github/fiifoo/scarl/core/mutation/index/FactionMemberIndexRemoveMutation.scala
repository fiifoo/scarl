package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{CreatureId, FactionId}

case class FactionMemberIndexRemoveMutation(member: CreatureId, faction: FactionId) {
  type Index = Map[FactionId, List[CreatureId]]

  def apply(index: Index): Index = {
    val members = index(faction) filter (_ != member)

    if (members.isEmpty) {
      index - faction
    } else {
      index + (faction -> members)
    }
  }
}
