package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Sector

case class CreatureSectorIndexRemoveMutation(creature: CreatureId, sector: Sector) {
  type Index = Map[Sector, Set[CreatureId]]

  def apply(index: Index): Index = {
    val creatures = index(sector) - creature

    if (creatures.isEmpty) {
      index - sector
    } else {
      index + (sector -> creatures)
    }
  }
}
