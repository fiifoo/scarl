package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.core.creature.Faction
import io.github.fiifoo.scarl.core.entity.Selectors.getContainerItems
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.{Location, WaypointNetwork}
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item.Recipe
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId
import io.github.fiifoo.scarl.core.kind.{ItemKindId, Kinds}
import io.github.fiifoo.scarl.game.RunState
import io.github.fiifoo.scarl.game.area.AreaInfo
import io.github.fiifoo.scarl.game.event.Event
import io.github.fiifoo.scarl.game.player.{PlayerFov, PlayerInfo, Settings}
import io.github.fiifoo.scarl.game.statistics.Statistics
import io.github.fiifoo.scarl.rule.SignalRule
import io.github.fiifoo.scarl.world._
import io.github.fiifoo.scarl.world.system.SolarSystem
import io.github.fiifoo.scarl.world.system.source.{SpaceshipSource, StellarBodySource}

sealed trait OutMessage

object DebugFov {
  def apply(state: RunState): DebugFov = {
    DebugFov(state.fov.locations)
  }
}

object DebugWaypoint {
  def apply(state: RunState): DebugWaypoint = {
    DebugWaypoint(state.instance.cache.waypointNetwork)
  }
}

object GameStart {
  def apply(state: RunState): GameStart = {
    val inventory = PlayerInventory(state)
    val world = WorldInfo(state)

    GameStart(
      area = AreaInfo(state),
      factions = state.game.world.assets.factions.values,
      kinds = state.game.world.assets.kinds,
      recipes = state.game.world.assets.recipes.values,
      settings = state.game.settings,
      spaceships = state.game.world.assets.spaceships.values,
      stellarBodies = state.game.world.assets.stellarBodies.values,
      // world
      site = world.site,
      regions = world.regions,
      sites = world.sites,
      transportRegions = world.transportRegions,
      transports = world.transports,
      system = world.system,
      // inventory
      equipments = inventory.equipments,
      inventory = inventory.inventory,
      playerRecipes = inventory.playerRecipes,
      recycledItems = inventory.recycledItems
    )
  }
}

object GameUpdate {
  def apply(state: RunState): GameUpdate = {
    GameUpdate(
      fov = state.fov,
      events = state.events,
      player = state.playerInfo,
    )
  }
}

object GameOver {
  def apply(state: RunState): GameOver = {
    GameOver(
      statistics = state.statistics
    )
  }
}

object AreaChange {
  def apply(state: RunState): AreaChange = {
    val inventory = PlayerInventory(state)
    val world = WorldInfo(state)

    AreaChange(
      area = AreaInfo(state),
      // world
      site = world.site,
      regions = world.regions,
      sites = world.sites,
      transportRegions = world.transportRegions,
      transports = world.transports,
      system = world.system,
      // inventory
      equipments = inventory.equipments,
      inventory = inventory.inventory,
      playerRecipes = inventory.playerRecipes,
      recycledItems = inventory.recycledItems
    )
  }
}

object PlayerInventory {
  def apply(state: RunState): PlayerInventory = {
    PlayerInventory(
      inventory = getContainerItems(state.instance)(state.game.player) map (_ (state.instance)),
      equipments = state.instance.equipments.getOrElse(state.game.player, Map()),
      playerRecipes = state.instance.recipes.getOrElse(state.game.player, Set()),
      recycledItems = state.instance.creature.recycledItems.getOrElse(state.game.player, List())
    )
  }
}

object SignalMap {
  def apply(state: RunState): SignalMap = {
    SignalMap(SignalRule.calculateMap(state.instance)(state.game.player))
  }
}

object WorldInfo {
  def apply(state: RunState): WorldInfo = {
    WorldInfo(
      site = state.game.area,
      regions = state.game.world.assets.regions,
      sites = state.game.world.assets.sites,
      transportRegions = state.game.world.transports,
      transports = state.game.world.assets.transports,
      system = state.game.world.system
    )
  }
}

case class DebugFov(locations: Set[Location]) extends OutMessage with DebugMessage

case class DebugWaypoint(network: WaypointNetwork) extends OutMessage with DebugMessage

case class PlayerSettings(settings: Settings) extends OutMessage

case class GameStart(area: AreaInfo,
                     factions: Iterable[Faction],
                     kinds: Kinds,
                     recipes: Iterable[Recipe],
                     settings: Settings,
                     spaceships: Iterable[SpaceshipSource],
                     stellarBodies: Iterable[StellarBodySource],
                     // world
                     site: SiteId,
                     regions: Map[RegionId, Region],
                     sites: Map[SiteId, Site],
                     transportRegions: Map[TransportId, RegionId],
                     transports: Map[TransportId, Transport],
                     system: SolarSystem,
                     // inventory
                     equipments: Map[Slot, ItemId],
                     inventory: Set[Item],
                     playerRecipes: Set[RecipeId],
                     recycledItems: List[ItemKindId],
                    ) extends OutMessage

case class GameUpdate(fov: PlayerFov,
                      events: List[Event],
                      player: PlayerInfo
                     ) extends OutMessage

case class GameOver(statistics: Statistics) extends OutMessage

case class AreaChange(area: AreaInfo,
                      //world
                      site: SiteId,
                      regions: Map[RegionId, Region],
                      sites: Map[SiteId, Site],
                      transportRegions: Map[TransportId, RegionId],
                      transports: Map[TransportId, Transport],
                      system: SolarSystem,
                      // inventory
                      equipments: Map[Slot, ItemId],
                      inventory: Set[Item],
                      playerRecipes: Set[RecipeId],
                      recycledItems: List[ItemKindId],
                     ) extends OutMessage

case class PlayerInventory(equipments: Map[Slot, ItemId],
                           inventory: Set[Item],
                           playerRecipes: Set[RecipeId],
                           recycledItems: List[ItemKindId],
                          ) extends OutMessage

case class SignalMap(signals: List[Signal]) extends OutMessage

case class WorldInfo(site: SiteId,
                     regions: Map[RegionId, Region],
                     sites: Map[SiteId, Site],
                     transportRegions: Map[TransportId, RegionId],
                     transports: Map[TransportId, Transport],
                     system: SolarSystem,
                    ) extends OutMessage
