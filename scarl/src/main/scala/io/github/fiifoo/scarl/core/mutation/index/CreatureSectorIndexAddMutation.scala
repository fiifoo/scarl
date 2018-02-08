package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Sector

case class CreatureSectorIndexAddMutation(creature: CreatureId, sector: Sector) {
  type Index = Map[Sector, Set[CreatureId]]

  def apply(index: Index): Index = {
    val creatures = if (index.isDefinedAt(sector)) {
      index(sector) + creature
    } else {
      Set(creature)
    }

    index + (sector -> creatures)
  }
}
