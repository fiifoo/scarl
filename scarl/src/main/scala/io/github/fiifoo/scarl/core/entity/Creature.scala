package fi.fiifoo.scarl.core.entity

import fi.fiifoo.scarl.core.Location

case class Creature(id: CreatureId,
                    location: Location,
                    tick: Int,
                    health: Int,
                    damage: Int
                   ) extends Entity with Actor with Locatable {

  def setLocation(location: Location): Locatable = copy(location = location)

  def setTick(tick: Int): Actor = copy(tick = tick)
}
