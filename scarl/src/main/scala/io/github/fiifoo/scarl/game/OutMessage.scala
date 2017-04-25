package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.{Creature, Item, ItemId}
import io.github.fiifoo.scarl.core.equipment.Slot
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.game.OutMessage.PlayerInfo
import io.github.fiifoo.scarl.game.map.MapLocation

object OutMessage {

  case class PlayerInfo(creature: Creature,
                        equipments: Map[Slot, ItemId],
                        equipmentStats: Creature.Stats,
                        inventory: Set[Item]
                       )

}

case class OutMessage(area: AreaId,
                      fov: PlayerFov,
                      messages: List[String],
                      player: Option[PlayerInfo],
                      kinds: Option[Kinds] = None,
                      map: Option[Map[Location, MapLocation]] = None,
                      statistics: Option[Statistics] = None
                     )
