package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.character.ProgressionId
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.{Location, State}

case class CreatureKind(id: CreatureKindId,
                        name: String,
                        display: Char,
                        color: String,
                        faction: FactionId,
                        progression: Option[ProgressionId],
                        stats: Creature.Stats,
                        greetings: Map[FactionId, List[CommunicationId]] = Map(),
                        responses: Map[FactionId, List[CommunicationId]] = Map()
                       ) extends Kind {

  def apply(s: State, location: Location): Creature = {
    Creature(
      id = CreatureId(s.nextEntityId),
      kind = id,
      faction = faction,
      progression = progression,
      location = location,
      tick = s.tick,
      damage = 0,
      experience = 0,
      level = 1,
      stats = stats
    )
  }
}
