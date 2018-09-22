package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.core.creature.Faction
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.{Location, WaypointNetwork}
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item.Recipe
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.game.area.AreaInfo
import io.github.fiifoo.scarl.game.event.Event
import io.github.fiifoo.scarl.game.player.{PlayerFov, PlayerInfo, Settings}
import io.github.fiifoo.scarl.game.statistics.Statistics

sealed trait OutMessage

case class DebugFov(locations: Set[Location]) extends OutMessage with DebugMessage

case class DebugWaypoint(network: WaypointNetwork) extends OutMessage with DebugMessage

case class GameStart(area: AreaInfo,
                     equipments: Map[Slot, ItemId],
                     factions: Iterable[Faction],
                     inventory: Set[Item],
                     kinds: Kinds,
                     playerRecipes: Set[RecipeId],
                     recipes: Iterable[Recipe],
                     settings: Settings
                    ) extends OutMessage

case class GameUpdate(fov: PlayerFov,
                      events: List[Event],
                      player: PlayerInfo
                     ) extends OutMessage

case class GameOver(statistics: Statistics) extends OutMessage

case class AreaChange(area: AreaInfo) extends OutMessage

case class PlayerSettings(settings: Settings) extends OutMessage

case class PlayerInventory(inventory: Set[Item],
                           equipments: Map[Slot, ItemId],
                           playerRecipes: Set[RecipeId]
                          ) extends OutMessage
