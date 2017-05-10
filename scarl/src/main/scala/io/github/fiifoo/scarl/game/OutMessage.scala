package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.character.Stats
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.equipment.Slot
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.game.OutMessage.PlayerInfo
import io.github.fiifoo.scarl.game.map.MapLocation

object OutMessage {

  case class PlayerInfo(creature: Creature,
                        equipments: Map[Slot, ItemId],
                        equipmentStats: Stats,
                        inventory: Set[Item]
                       )

}

case class OutMessage(area: AreaId,
                      factions: Option[Iterable[Faction]] = None,
                      fov: PlayerFov,
                      messages: List[String],
                      player: Option[PlayerInfo],
                      kinds: Option[Kinds] = None,
                      map: Option[Map[Location, MapLocation]] = None,
                      statistics: Option[Statistics] = None
                     )
