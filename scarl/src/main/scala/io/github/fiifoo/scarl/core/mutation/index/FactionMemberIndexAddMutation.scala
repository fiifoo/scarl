package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{CreatureId, FactionId}

case class FactionMemberIndexAddMutation(member: CreatureId, faction: FactionId) {
  type Index = Map[FactionId, List[CreatureId]]

  def apply(index: Index): Index = {
    val members = if (index.isDefinedAt(faction)) {
      member :: index(faction)
    } else {
      List(member)
    }

    index + (faction -> members)
  }
}
