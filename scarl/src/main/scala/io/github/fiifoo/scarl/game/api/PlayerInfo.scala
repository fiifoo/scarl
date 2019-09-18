package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.entity.{CreatureId, Item, ItemId, Selectors}
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item.Key
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.game.RunState

object PlayerInfo {
  def apply(state: RunState): PlayerInfo = {
    val previous = state.previous map (PlayerInfo(_))

    create(state.instance, state.game.player, previous)
  }

  private def create(s: State, creature: CreatureId, previous: Option[PlayerInfo]): PlayerInfo = {
    val equipments = s.creature.equipments.getOrElse(creature, Map())
    val equipmentStats = Selectors.getEquipmentStats(s)(creature)
    val inventory = Selectors.getContainerItems(s)(creature) map (_ (s))
    val keys = Selectors.getCreatureKeys(s)(creature)
    val playerRecipes = s.creature.recipes.getOrElse(creature, Set())
    val recycledItems = s.creature.recycledItems.getOrElse(creature, List())

    def changed[T](value: T, extract: PlayerInfo => Option[T]): Option[T] = {
      if (previous exists (extract(_).contains(value))) None else Some(value)
    }

    PlayerInfo(
      conversation = s.creature.conversations.get(creature) map (_._2),
      creature = CreatureInfo(s)(creature),
      equipments = changed(equipments, _.equipments),
      equipmentStats = changed(equipmentStats, _.equipmentStats),
      inventory = changed(inventory, _.inventory),
      keys = changed(keys, _.keys),
      playerRecipes = changed(playerRecipes, _.playerRecipes),
      recycledItems = changed(recycledItems, _.recycledItems),
    )
  }
}

case class PlayerInfo(conversation: Option[CommunicationId],
                      creature: CreatureInfo,
                      equipments: Option[Map[Slot, ItemId]],
                      equipmentStats: Option[Stats],
                      inventory: Option[Set[Item]],
                      keys: Option[Set[Key]],
                      playerRecipes: Option[Set[RecipeId]],
                      recycledItems: Option[List[ItemKindId]],
                     )
