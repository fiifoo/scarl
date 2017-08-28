package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.action.Behavior
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.{Character, FactionId, Missile, Party, Stats}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.Kind.Result
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.power.CreaturePowerId
import io.github.fiifoo.scarl.core.{IdSeq, Location, State}

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
                       ) extends Kind[Creature] {

  def toLocation(s: State, idSeq: IdSeq, location: Location, owner: Option[SafeCreatureId]): Result[Creature] = {
    val (nextId, nextIdSeq) = idSeq()
    val creatureId = CreatureId(nextId)

    val creature = Creature(
      id = creatureId,
      kind = id,
      faction = faction,
      solitary = solitary,
      party = getParty(s, location, creatureId),
      behavior = behavior,
      location = location,
      tick = s.tick,
      damage = 0,
      stats = stats,
      owner = owner,

      character = character,
      flying = flying,
      missile = missile,
      usable = usable
    )

    Result(
      mutations = List(IdSeqMutation(nextIdSeq), NewEntityMutation(creature)),
      idSeq = nextIdSeq,
      entity = creature,
    )
  }

  def toLocation(s: State, idSeq: IdSeq, location: Location): Result[Creature] = {
    toLocation(s, idSeq, location, None)
  }

  private def getParty(s: State, location: Location, self: CreatureId): Party = {
    if (solitary) {
      Party(self)
    } else {
      Party.find(s, this, location) getOrElse Party(self)
    }
  }
}
