package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.ai.Behavior
import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.creature._
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Selectors.getLocationEntities
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId, TerrainKindId, WallKindId}
import io.github.fiifoo.scarl.core.{State, Tag}

sealed trait Entity {
  val id: EntityId
}

sealed trait Actor extends Entity {
  val id: ActorId
  val tick: Tick

  def setTick(tick: Tick): Actor
}

sealed trait Chargeable extends Entity {
  val charge: Option[Charge]

  def setCharge(charge: Option[Charge]): Chargeable
}

sealed trait Locatable extends Entity {
  val id: LocatableId
  val location: Location

  def setLocation(location: Location): Locatable
}

sealed trait Lockable extends Entity {
  val id: LockableId
  val locked: Option[Lock]

  def setLocked(locked: Option[Lock]): Lockable
}

sealed trait Usable extends Lockable {
  val id: UsableId
  val usable: Option[Power]
}

sealed trait Taggable extends Entity {
  val tags: Set[Tag]
}

trait Status extends Entity {
  val id: StatusId
  val target: EntityId
}

trait ChargeableStatus extends Entity with Status with Chargeable {
  def setCharge(charge: Option[Charge]): ChargeableStatus
}

trait ConditionStatus extends Entity with Status {
  val condition: Condition
  val strength: Int

  def setStrength(strength: Int): ConditionStatus
}

trait StanceStatus extends Entity with Status {
  val stance: Stance
  val continuous: Boolean
  val duration: Option[Int]
  val active: Boolean

  def setDuration(duration: Option[Int]): StanceStatus

  def setActive(active: Boolean): StanceStatus
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
                     faction: Option[FactionId] = None,
                     owner: Option[SafeCreatureId] = None,
                     tags: Set[Tag] = Set(),
                     widget: Boolean = false,
                    ) extends Entity with Locatable with Taggable {

  def setLocation(location: Location): Container = copy(location = location)
}

case class Creature(id: CreatureId,
                    kind: CreatureKindId,
                    faction: FactionId,
                    behavior: Behavior,
                    stats: Stats,

                    character: Option[Character] = None,
                    locked: Option[Lock] = None,
                    usable: Option[CreaturePower] = None,
                    traits: Traits = Traits(),

                    damage: Double = 0,
                    dead: Boolean = false,
                    location: Location,
                    owner: Option[SafeCreatureId] = None,
                    party: Party,
                    resources: Resources = Resources(),
                    tags: Set[Tag] = Set(),
                    tick: Tick,
                   ) extends Entity with Actor with Locatable with Usable with Taggable {

  def setLocation(location: Location): Creature = copy(location = location)

  def setLocked(locked: Option[Lock]): Creature = copy(locked = locked)

  def setTick(tick: Tick): Creature = copy(tick = tick)

  def leader: Option[SafeCreatureId] = {
    if (party.leader == SafeCreatureId(id)) {
      None
    } else {
      Some(party.leader)
    }
  }
}

case class GlobalActor(id: GlobalActorId, tick: Tick) extends Entity with Actor {
  def setTick(tick: Tick): GlobalActor = copy(tick = tick)
}

case class Item(id: ItemId,
                kind: ItemKindId,
                container: EntityId,
                tags: Set[Tag] = Set(),

                concealment: Int = 0,
                hidden: Boolean = false,
                locked: Option[Lock] = None,
                pickable: Boolean = false,
                recyclable: Option[Int] = None,

                armor: Option[Armor] = None,
                door: Option[Door] = None,
                explosive: Option[Explosive] = None,
                key: Option[Key] = None,
                launcher: Option[MissileLauncher] = None,
                rangedWeapon: Option[RangedWeapon] = None,
                shield: Option[Shield] = None,
                trap: Option[ItemPower] = None,
                usable: Option[ItemPower] = None,
                weapon: Option[Weapon] = None,
               ) extends Entity with Usable with Taggable {

  def setLocked(locked: Option[Lock]): Item = copy(locked = locked)

  def isLockedDoor: Boolean = this.door.exists(!_.open) && this.locked.isDefined
}

case class Machinery(id: MachineryId,
                     mechanism: Mechanism,
                     controls: Set[Location] = Set(),
                     targets: Set[Location] = Set(),
                     tag: Option[Tag] = None,
                     description: Option[String] = None,
                     disposable: Boolean = false
                    ) extends Entity {

  def interact(s: State, control: Location): List[Effect] = mechanism.interact(s, this, control)

  def getTargetEntities(s: State): Set[Locatable] = {
    if (this.targets.isEmpty) {
      this.tag map this.collectTagEntities(s.entities.values) getOrElse Set()
    } else {
      val entities: Set[Locatable] = this.targets flatMap getLocationEntities(s) map (_.apply(s))

      this.tag map this.collectTagEntities(entities) getOrElse entities
    }
  }

  private def collectTagEntities(entities: Iterable[Entity])(tag: Tag): Set[Locatable] = {
    (entities collect {
      case entity: Locatable with Taggable if entity.tags.contains(tag) => entity
    }).toSet
  }
}

case class Terrain(id: TerrainId,
                   kind: TerrainKindId,
                   location: Location,
                   tags: Set[Tag] = Set(),
                   impassable: Boolean = false,
                  ) extends Entity with Locatable with Taggable {

  def setLocation(location: Location): Terrain = copy(location = location)
}

case class Wall(id: WallId,
                kind: WallKindId,
                location: Location,
                tags: Set[Tag] = Set(),
                hardness: Option[Int] = None,
                transparent: Boolean = false,
               ) extends Entity with Locatable with Taggable {

  def setLocation(location: Location): Wall = copy(location = location)
}
