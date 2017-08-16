package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.action.Behavior
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.{Character, FactionId, Missile, Party, Stats}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.power.CreaturePowerId
import io.github.fiifoo.scarl.core.{Location, State}

case class CreatureKind(id: CreatureKindId,
                        name: String,
                        display: Char,
                        color: String,
                        faction: FactionId,
                        solitary: Boolean = false,
                        behavior: Behavior,
                        stats: Stats,

                        character: Option[Character] = None,
                        flying: Boolean = false,
                        missile: Option[Missile] = None,
                        usable: Option[CreaturePowerId] = None,

                        greetings: Map[FactionId, List[CommunicationId]] = Map(),
                        responses: Map[FactionId, List[CommunicationId]] = Map()
                       ) extends Kind {

  def apply(s: State, location: Location, party: Option[Party] = None): Creature = {
    val creatureId = CreatureId(s.nextEntityId)

    Creature(
      id = creatureId,
      kind = id,
      faction = faction,
      solitary = solitary,
      party = party getOrElse getParty(s, location, creatureId),
      behavior = behavior,
      location = location,
      tick = s.tick,
      damage = 0,
      stats = stats,

      character = character,
      flying = flying,
      missile = missile,
      usable = usable
    )
  }

  private def getParty(s: State, location: Location, self: CreatureId): Party = {
    if (solitary) {
      Party(self)
    } else {
      Party.find(s, this, location) getOrElse Party(self)
    }
  }
}
