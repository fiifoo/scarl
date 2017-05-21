package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.character.Stats
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.game.api.OutMessage.PlayerInfo
import io.github.fiifoo.scarl.game.map.MapLocation
import io.github.fiifoo.scarl.game.{PlayerFov, Statistics}

object OutMessage {

  case class PlayerInfo(creature: Creature,
                        equipmentStats: Stats
                       )

}

sealed trait OutMessage

case class GameStart(area: AreaId,
                     factions: Iterable[Faction],
                     kinds: Kinds,
                     map: Map[Location, MapLocation]
                    ) extends OutMessage

case class GameUpdate(fov: PlayerFov,
                      messages: List[String],
                      player: PlayerInfo
                     ) extends OutMessage

case class GameOver(statistics: Statistics) extends OutMessage

case class AreaChange(area: AreaId,
                      map: Map[Location, MapLocation]
                     ) extends OutMessage

case class PlayerInventory(inventory: Set[Item],
                           equipments: Map[Slot, ItemId]
                          ) extends OutMessage
