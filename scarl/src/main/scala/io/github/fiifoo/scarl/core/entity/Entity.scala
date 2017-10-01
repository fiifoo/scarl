package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.action.Behavior
import io.github.fiifoo.scarl.core.creature.{Character, FactionId, Missile, Party, Stats}
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId, TerrainKindId, WallKindId}
import io.github.fiifoo.scarl.core.power.{CreaturePowerId, ItemPowerId}
import io.github.fiifoo.scarl.core.{Location, State}

sealed trait Entity {
  val id: EntityId
}

sealed trait Actor extends Entity {
  val id: ActorId
  val tick: Int

  def setTick(tick: Int): Actor
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

  def setTick(tick: Int): ActiveStatus
}

trait PassiveStatus extends Entity with Status {
  val id: PassiveStatusId
}

trait TriggerStatus extends Entity with Status {
  val id: TriggerStatusId
  val target: ContainerId

  def apply(s: State, triggerer: CreatureId): List[Effect]
}

case class Container(id: ContainerId, location: Location) extends Entity with Locatable {

  def setLocation(location: Location): Container = copy(location = location)
}

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
                    usable: Option[CreaturePowerId] = None,
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

case class Item(id: ItemId,
                kind: ItemKindId,
                container: EntityId,

                armor: Option[Armor] = None,
                door: Option[Door] = None,
                hidden: Boolean = false,
                pickable: Boolean = false,
                rangedWeapon: Option[RangedWeapon] = None,
                shield: Option[Shield] = None,
                usable: Option[ItemPowerId] = None,
                weapon: Option[Weapon] = None,
               ) extends Entity

case class Terrain(id: TerrainId,
                   kind: TerrainKindId,
                   location: Location,
                  ) extends Entity with Locatable {

  def setLocation(location: Location): Terrain = copy(location = location)
}

case class Wall(id: WallId,
                kind: WallKindId,
                location: Location,
               ) extends Entity with Locatable {

  def setLocation(location: Location): Wall = copy(location = location)
}
