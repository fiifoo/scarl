package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.{Location, State}

case class CreatureKind(id: CreatureKindId,
                        name: String,
                        display: Char,
                        color: String,
                        faction: FactionId,
                        stats: Creature.Stats
                       ) extends Kind {

  def apply(s: State, location: Location): Creature = {
    Creature(
      id = CreatureId(s.nextEntityId),
      kind = id,
      faction = faction,
      location = location,
      tick = s.tick,
      damage = 0,
      experience = 0,
      stats = stats
    )
  }
}
