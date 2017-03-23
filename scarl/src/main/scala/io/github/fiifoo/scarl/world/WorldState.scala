package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.{Area, AreaId, Conduit}
import io.github.fiifoo.scarl.core.{ConduitId, State}

case class WorldState(areas: Map[AreaId, Area],
                      conduits: Map[ConduitId, Conduit] = Map(),
                      nextConduitId: Int = 1,
                      states: Map[AreaId, State] = Map()
                     )
