package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.{Character, FactionId, Missile, Stats}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.{Location, State}

case class CreatureKind(id: CreatureKindId,
                        name: String,
                        display: Char,
                        color: String,
                        faction: FactionId,
                        stats: Stats,
                        character: Option[Character] = None,
                        missile: Option[Missile] = None,
                        flying: Boolean = false,
                        greetings: Map[FactionId, List[CommunicationId]] = Map(),
                        responses: Map[FactionId, List[CommunicationId]] = Map()
                       ) extends Kind {

  def apply(s: State, location: Location): Creature = {
    Creature(
      id = CreatureId(s.nextEntityId),
      kind = id,
      faction = faction,
      location = location,
      tick = s.tick,
      damage = 0,
      stats = stats,
      character = character,
      missile = missile,
      flying = flying
    )
  }
}
