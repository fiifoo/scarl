package fi.fiifoo.scarl.core.entity

trait Status extends Entity {
  val id: StatusId
  val target: EntityId
}
