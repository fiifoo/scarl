package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.world.WorldState

case class GameState(area: AreaId,
                     player: CreatureId,
                     world: WorldState
                    )
