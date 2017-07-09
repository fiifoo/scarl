package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.creature._
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.power.CreaturePowerId

case class Creature(id: CreatureId,
                    kind: CreatureKindId,
                    faction: FactionId,
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
}
