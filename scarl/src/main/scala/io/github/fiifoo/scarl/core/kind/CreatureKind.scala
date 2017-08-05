package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.action.Behavior
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.{Character, FactionId, Missile, Stats}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.power.CreaturePowerId
import io.github.fiifoo.scarl.core.{Location, State}

case class CreatureKind(id: CreatureKindId,
                        name: String,
                        display: Char,
                        color: String,
                        faction: FactionId,
                        behavior: Behavior,
                        stats: Stats,

                        character: Option[Character] = None,
                        flying: Boolean = false,
                        missile: Option[Missile] = None,
                        usable: Option[CreaturePowerId] = None,

                        greetings: Map[FactionId, List[CommunicationId]] = Map(),
                        responses: Map[FactionId, List[CommunicationId]] = Map()
                       ) extends Kind {

  def apply(s: State, location: Location): Creature = {
    Creature(
      id = CreatureId(s.nextEntityId),
      kind = id,
      faction = faction,
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
}
