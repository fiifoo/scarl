package io.github.fiifoo.scarl.core.world

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Selectors.{getContainerItems, getTargetStatuses}
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

object Traveler {

  def apply(s: State, creature: CreatureId): Traveler = {
    // Items inside statuses not supported. Don't allow at model?
    val items = getItems(s, creature)
    // Supports statuses targeting statuses. Really needed?
    val statuses = getStatuses(s, creature) ++ (items flatMap (getStatuses(s, _)))

    Traveler(
      creature = creature(s),
      items = items map (_ (s)),
      statuses = statuses map (_ (s)),
      equipments = s.equipments getOrElse(creature, Map()),
      keys = s.keys.getOrElse(creature, Set()),
      recipes = s.recipes.getOrElse(creature, Set())
    )
  }

  private def getItems(s: State, container: EntityId): Set[ItemId] = {
    val items = getContainerItems(s)(container)

    if (items.nonEmpty) {
      items ++ (items flatMap (getItems(s, _)))
    } else {
      Set()
    }
  }

  private def getStatuses(s: State, target: EntityId): Set[StatusId] = {
    val statuses = getTargetStatuses(s)(target)

    if (statuses.nonEmpty) {
      statuses ++ (statuses flatMap (getStatuses(s, _)))
    } else {
      Set()
    }
  }
}
