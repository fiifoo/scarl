package io.github.fiifoo.scarl.core.entity

trait Status extends Entity {
  val id: StatusId
  val target: EntityId
}
