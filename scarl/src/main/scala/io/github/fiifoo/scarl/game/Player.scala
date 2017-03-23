package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.CreatureId

class Player(var creature: CreatureId,
             var fov: Set[Location] = Set()
            )
