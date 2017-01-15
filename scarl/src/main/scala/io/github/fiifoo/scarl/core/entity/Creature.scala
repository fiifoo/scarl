package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.Location

case class Creature(id: CreatureId,
                    location: Location,
                    tick: Int,
                    health: Int,
                    damage: Int
                   ) extends Entity with Actor with Locatable {

  def setLocation(location: Location): Locatable = copy(location = location)

  def setTick(tick: Int): Actor = copy(tick = tick)
}
