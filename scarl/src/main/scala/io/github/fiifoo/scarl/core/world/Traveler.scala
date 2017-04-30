package io.github.fiifoo.scarl.core.world

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.equipment.Slot

case class Traveler(creature: Creature,
                    items: Set[Item],
                    statuses: Set[Status],
                    equipments: Map[Slot, ItemId]
                   )
