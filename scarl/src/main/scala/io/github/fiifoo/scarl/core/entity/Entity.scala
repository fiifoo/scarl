package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.ai.Behavior
import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.creature.{Character, CreaturePower, FactionId, Missile, Party, Stats}
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId, TerrainKindId, WallKindId}

sealed trait Entity {
  val id: EntityId
}

sealed trait Actor extends Entity {
  val id: ActorId
  val tick: Tick

  def setTick(tick: Tick): Actor
}

sealed trait Locatable extends Entity {
  val id: LocatableId
  val location: Location

  def setLocation(location: Location): Locatable
}

trait Status extends Entity {
  val id: StatusId
  val target: EntityId
}

trait ActiveStatus extends Entity with Status with Actor {
  val id: ActiveStatusId

  def apply(s: State): List[Effect]

  def setTick(tick: Tick): ActiveStatus
}

trait PassiveStatus extends Entity with Status {
  val id: PassiveStatusId
}

trait TriggerStatus extends Entity with Status {
  val id: TriggerStatusId
  val target: ContainerId

  def apply(s: State, triggerer: CreatureId): List[Effect]
}

case class Container(id: ContainerId,
                     location: Location,
                     owner: Option[SafeCreatureId] = None
                    ) extends Entity with Locatable {

  def setLocation(location: Location): Container = copy(location = location)
}

case class Creature(id: CreatureId,
                    kind: CreatureKindId,
                    faction: FactionId,
                    solitary: Boolean = false,
                    party: Party,
                    behavior: Behavior,
                    location: Location,
                    tick: Tick,
                    damage: Double = 0,
                    energy: Double = 0,
                    materials: Double = 0,
                    stats: Stats,
                    dead: Boolean = false,
                    owner: Option[SafeCreatureId] = None,

                    character: Option[Character] = None,
                    flying: Boolean = false,
                    missile: Option[Missile] = None,
                    usable: Option[CreaturePower] = None,
                   ) extends Entity with Actor with Locatable {

  def setLocation(location: Location): Creature = copy(location = location)

  def setTick(tick: Tick): Creature = copy(tick = tick)

  def leader: Option[CreatureId] = {
    if (party.leader == id) {
      None
    } else {
      Some(party.leader)
    }
  }
}

case class Item(id: ItemId,
                kind: ItemKindId,
                container: EntityId,

                armor: Option[Armor] = None,
                door: Option[Door] = None,
                explosive: Option[Explosive] = None,
                hidden: Boolean = false,
                key: Option[Key] = None,
                lock: Option[Key] = None,
                personal: Boolean = false,
                pickable: Boolean = false,
                rangedWeapon: Option[RangedWeapon] = None,
                shield: Option[Shield] = None,
                usable: Option[ItemPower] = None,
                weapon: Option[Weapon] = None,
               ) extends Entity

case class Machinery(id: MachineryId,
                     mechanism: Mechanism,
                     controls: Set[Location],
                     targets: Set[Location]
                    ) extends Entity {

  def interact(s: State, control: Location): List[Effect] = mechanism.interact(s, this, control)
}

case class Terrain(id: TerrainId,
                   kind: TerrainKindId,
                   location: Location,
                  ) extends Entity with Locatable {

  def setLocation(location: Location): Terrain = copy(location = location)
}

case class Wall(id: WallId,
                kind: WallKindId,
                location: Location,
                hardness: Option[Int] = None,
               ) extends Entity with Locatable {

  def setLocation(location: Location): Wall = copy(location = location)
}
