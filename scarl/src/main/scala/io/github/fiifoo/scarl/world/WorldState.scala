package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.world.ConduitId

case class WorldState(assets: WorldAssets = WorldAssets(),
                      conduits: Map[ConduitId, Conduit] = Map(),
                      nextConduitId: Int = 1,
                      states: Map[AreaId, State] = Map()
                     )
