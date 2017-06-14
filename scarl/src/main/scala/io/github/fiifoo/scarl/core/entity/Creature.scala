package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.creature.{Character, FactionId, Missile, Stats}
import io.github.fiifoo.scarl.core.kind.CreatureKindId

case class Creature(id: CreatureId,
                    kind: CreatureKindId,
                    faction: FactionId,
                    location: Location,
                    tick: Int,
                    damage: Int,
                    stats: Stats,
                    owner: Option[SafeCreatureId] = None,
                    character: Option[Character] = None,
                    missile: Option[Missile] = None,
                    flying: Boolean = false
                   ) extends Entity with Actor with Locatable {

  def setLocation(location: Location): Creature = copy(location = location)

  def setTick(tick: Int): Creature = copy(tick = tick)
}
