package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.character.ProgressionId
import io.github.fiifoo.scarl.core.entity.Creature.Stats
import io.github.fiifoo.scarl.core.kind.CreatureKindId

object Creature {

  case class Stats(health: Int,
                   attack: Int,
                   defence: Int,
                   damage: Int,
                   armor: Int,
                   sight: Sight
                  ) {

    def add(stats: Stats): Stats = {
      copy(
        health + stats.health,
        attack + stats.attack,
        defence + stats.defence,
        damage + stats.damage,
        armor + stats.armor,
        sight = stats.sight.add(sight)
      )
    }
  }

  case class Sight(range: Int) {
    def add(sight: Sight): Sight = {
      copy(range + sight.range)
    }
  }

}

case class Creature(id: CreatureId,
                    kind: CreatureKindId,
                    faction: FactionId,
                    progression: Option[ProgressionId],
                    location: Location,
                    tick: Int,
                    damage: Int,
                    experience: Int,
                    level: Int,
                    stats: Stats
                   ) extends Entity with Actor with Locatable {

  def setLocation(location: Location): Locatable = copy(location = location)

  def setTick(tick: Int): Actor = copy(tick = tick)
}
