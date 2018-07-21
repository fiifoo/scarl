package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

sealed trait EntityId {
  val value: Int

  def apply(s: State): Entity = s.entities(this)
}

sealed trait ActorId extends EntityId {
  override def apply(s: State): Actor = s.entities(this).asInstanceOf[Actor]
}

sealed trait LocatableId extends EntityId {
  override def apply(s: State): Locatable = s.entities(this).asInstanceOf[Locatable]
}

sealed trait LockableId extends EntityId {
  override def apply(s: State): Lockable = s.entities(this).asInstanceOf[Lockable]
}

sealed trait UsableId extends LockableId {
  override def apply(s: State): Usable = s.entities(this).asInstanceOf[Usable]
}

sealed trait StatusId extends EntityId {
  override def apply(s: State): Status = s.entities(this).asInstanceOf[Status]
}

case class ActiveStatusId(value: Int) extends EntityId with StatusId with ActorId {
  override def apply(s: State): ActiveStatus = s.entities(this).asInstanceOf[ActiveStatus]
}

case class ContainerId(value: Int) extends EntityId with LocatableId {
  override def apply(s: State): Container = s.entities(this).asInstanceOf[Container]
}

case class CreatureId(value: Int) extends EntityId with ActorId with LocatableId with UsableId {
  override def apply(s: State): Creature = s.entities(this).asInstanceOf[Creature]
}

case class ItemId(value: Int) extends EntityId with UsableId {
  override def apply(s: State): Item = s.entities(this).asInstanceOf[Item]
}

case class MachineryId(value: Int) extends EntityId {
  override def apply(s: State): Machinery = s.entities(this).asInstanceOf[Machinery]
}

case class PassiveStatusId(value: Int) extends EntityId with StatusId {
  override def apply(s: State): PassiveStatus = s.entities(this).asInstanceOf[PassiveStatus]
}

case class TerrainId(value: Int) extends EntityId with LocatableId {
  override def apply(s: State): Terrain = s.entities(this).asInstanceOf[Terrain]
}

case class TriggerStatusId(value: Int) extends EntityId with StatusId {
  override def apply(s: State): TriggerStatus = s.entities(this).asInstanceOf[TriggerStatus]
}

case class WallId(value: Int) extends EntityId with LocatableId {
  override def apply(s: State): Wall = s.entities(this).asInstanceOf[Wall]
}
