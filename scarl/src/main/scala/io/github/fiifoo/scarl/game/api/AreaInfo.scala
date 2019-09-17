package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.game.RunState
import io.github.fiifoo.scarl.game.area.MapLocation
import io.github.fiifoo.scarl.world.SiteId

object AreaInfo {
  def apply(state: RunState): AreaInfo = {
    AreaInfo(
      id = state.game.area,
      width = state.instance.area.width,
      height = state.instance.area.height,
      map = state.areaMap
    )
  }
}

case class AreaInfo(id: SiteId, width: Int, height: Int, map: Map[Location, MapLocation])
