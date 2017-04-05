package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.game.map.MapLocation

case class OutMessage(area: AreaId,
                      fov: PlayerFov,
                      messages: List[String],
                      player: Option[Creature],
                      kinds: Option[Kinds] = None,
                      map: Option[Map[Location, MapLocation]] = None,
                      statistics: Option[Statistics] = None
                     )
