package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.core.creature.{FactionId, Stats}
import io.github.fiifoo.scarl.core.kind.{CreatureKind, CreatureKindId}

object Combatant {

  def apply(kind: CreatureKind): Combatant = {
    Combatant(CreatureKindId(kind.id.value), kind.stats)
  }

}

case class Combatant(id: CreatureKindId, stats: Stats) {

  def apply(faction: FactionId): CreatureKind = {
    CreatureKind(
      id = id,
      name = id.value,
      display = 'c',
      color = "white",
      faction = faction,
      stats = stats,
    )
  }

}
