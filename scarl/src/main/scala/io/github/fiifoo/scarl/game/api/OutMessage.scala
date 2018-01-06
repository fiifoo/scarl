package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.creature.Faction
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.{Location, WaypointNetwork}
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.game.event.Event
import io.github.fiifoo.scarl.game.map.MapLocation
import io.github.fiifoo.scarl.game.player.{PlayerFov, PlayerInfo}
import io.github.fiifoo.scarl.game.statistics.Statistics

sealed trait OutMessage

case class DebugFov(locations: Set[Location]) extends OutMessage with DebugMessage

case class DebugWaypoint(network: WaypointNetwork) extends OutMessage with DebugMessage

case class GameStart(area: AreaId,
                     factions: Iterable[Faction],
                     kinds: Kinds,
                     map: Map[Location, MapLocation]
                    ) extends OutMessage

case class GameUpdate(fov: PlayerFov,
                      events: List[Event],
                      player: PlayerInfo
                     ) extends OutMessage

case class GameOver(statistics: Statistics) extends OutMessage

case class AreaChange(area: AreaId,
                      map: Map[Location, MapLocation]
                     ) extends OutMessage

case class PlayerInventory(inventory: Set[Item],
                           equipments: Map[Slot, ItemId]
                          ) extends OutMessage
