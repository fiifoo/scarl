package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.core.creature.Faction
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.{Location, WaypointNetwork}
import io.github.fiifoo.scarl.core.item.Recipe
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.effect.interact.ReceiveCommunicationEffect
import io.github.fiifoo.scarl.game.RunState
import io.github.fiifoo.scarl.game.event.{Event, EventBuilder}
import io.github.fiifoo.scarl.game.player.{PlayerFov, Settings}
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
    val world = WorldInfo(state)

    val events = state.instance.creature.conversations.get(state.game.player) map (x => {
      val (source, communication) = x

      EventBuilder(state.instance, state.game.player, Set(), List(
        ReceiveCommunicationEffect(source, state.game.player, communication, state.game.player(state.instance).location)
      ))
    }) getOrElse {
      List()
    }

    GameStart(
      area = AreaInfo(state),
      events = events,
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
    )
  }
}

object GameUpdate {
  def apply(state: RunState): GameUpdate = {
    val factionInfo = FactionInfo(state)
    val playerInfo = PlayerInfo(state)

    GameUpdate(
      fov = state.fov,
      events = state.events,
      player = playerInfo,
      factionInfo = if (state.previous.exists(FactionInfo(_) == factionInfo)) None else Some(factionInfo)
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
                     events: List[Event],
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
                    ) extends OutMessage

case class GameUpdate(fov: PlayerFov,
                      events: List[Event],
                      player: PlayerInfo,
                      factionInfo: Option[FactionInfo],
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
                     ) extends OutMessage

case class SignalMap(signals: List[Signal]) extends OutMessage

case class WorldInfo(site: SiteId,
                     regions: Map[RegionId, Region],
                     sites: Map[SiteId, Site],
                     transportRegions: Map[TransportId, RegionId],
                     transports: Map[TransportId, Transport],
                     system: SolarSystem,
                    ) extends OutMessage
