package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.action.Behavior
import io.github.fiifoo.scarl.core.creature._
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.power.CreaturePowerId

case class Creature(id: CreatureId,
                    kind: CreatureKindId,
                    faction: FactionId,
                    solitary: Boolean = false,
                    party: Party,
                    behavior: Behavior,
                    location: Location,
                    tick: Int,
                    damage: Int,
                    stats: Stats,
                    dead: Boolean = false,
                    owner: Option[SafeCreatureId] = None,

                    character: Option[Character] = None,
                    flying: Boolean = false,
                    missile: Option[Missile] = None,
                    usable: Option[CreaturePowerId] = None
                   ) extends Entity with Actor with Locatable {

  def setLocation(location: Location): Creature = copy(location = location)

  def setTick(tick: Int): Creature = copy(tick = tick)

  def leader: Option[CreatureId] = {
    if (party.leader == id) {
      None
    } else {
      Some(party.leader)
    }
  }
}
