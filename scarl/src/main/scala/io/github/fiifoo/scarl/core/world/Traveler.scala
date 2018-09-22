package io.github.fiifoo.scarl.core.world

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item.Key
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId

case class Traveler(creature: Creature,
                    items: Set[Item],
                    statuses: Set[Status],
                    equipments: Map[Slot, ItemId],
                    keys: Set[Key],
                    recipes: Set[RecipeId]
                   )
