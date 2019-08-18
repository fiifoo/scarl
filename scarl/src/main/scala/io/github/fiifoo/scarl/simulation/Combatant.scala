package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.core.ai.Behavior
import io.github.fiifoo.scarl.core.creature.{FactionId, Stats}
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.kind.{CreatureKind, CreatureKindId, ItemKind}

object Combatant {

  def apply(kind: CreatureKind): Combatant = {
    Combatant(CreatureKindId(kind.id.value), kind.stats, kind.behavior)
  }

  def withEquipment(kind: CreatureKind, equipments: Map[Slot, ItemKind]): Combatant = {
    Combatant(CreatureKindId(kind.id.value), kind.stats, kind.behavior, equipments)
  }
}

case class Combatant(id: CreatureKindId,
                     stats: Stats,
                     behavior: Behavior,
                     equipments: Map[Slot, ItemKind] = Map()
                    ) {

  def apply(faction: FactionId): CreatureKind = {
    CreatureKind(
      id = id,
      name = id.value,
      display = 'c',
      color = "white",
      faction = faction,
      behavior = behavior,
      stats = stats,
      equipments = equipments transform ((_, item) => item.id)
    )
  }

}
