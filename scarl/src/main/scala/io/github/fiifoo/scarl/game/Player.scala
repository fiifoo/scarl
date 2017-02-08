package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.CreatureId

class Player(val creature: CreatureId = CreatureId(1),
             var fov: Set[Location] = Set()
            )
