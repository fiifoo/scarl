package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.Creature.Stats
import io.github.fiifoo.scarl.core.kind.CreatureKindId

object Creature {

  case class Stats(health: Int,
                   attack: Int,
                   defence: Int,
                   damage: Int,
                   armor: Int,
                   sight: Sight
                  )

  case class Sight(range: Int)

}

case class Creature(id: CreatureId,
                    kind: CreatureKindId,
                    faction: FactionId,
                    location: Location,
                    tick: Int,
                    damage: Int,
                    experience: Int,
                    stats: Stats
                   ) extends Entity with Actor with Locatable {

  def setLocation(location: Location): Locatable = copy(location = location)

  def setTick(tick: Int): Actor = copy(tick = tick)
}
