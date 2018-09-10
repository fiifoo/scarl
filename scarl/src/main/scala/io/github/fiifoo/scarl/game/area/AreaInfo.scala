package io.github.fiifoo.scarl.game.area

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.game.RunState

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

case class AreaInfo(id: AreaId, width: Int, height: Int, map: Map[Location, MapLocation])
