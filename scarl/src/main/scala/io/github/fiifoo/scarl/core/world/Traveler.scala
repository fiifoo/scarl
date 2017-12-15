package io.github.fiifoo.scarl.core.world

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item.Key

case class Traveler(creature: Creature,
                    items: Set[Item],
                    statuses: Set[Status],
                    equipments: Map[Slot, ItemId],
                    keys: Set[Key]
                   )
