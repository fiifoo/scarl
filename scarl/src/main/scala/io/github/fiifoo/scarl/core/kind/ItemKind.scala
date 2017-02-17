package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.{Location, State}

case class ItemKind(id: ItemKindId,
                    name: String,
                    display: Char,
                    color: String
                   ) extends Kind {

  def apply(s: State, container: EntityId): Item = {
    Item(
      id = ItemId(s.nextEntityId),
      kind = id,
      container = container
    )
  }

  def apply(s: State, location: Location): (Container, Item) = {
    val container = Container(ContainerId(s.nextEntityId), location)

    val item = Item(
      id = ItemId(s.nextEntityId + 1),
      kind = id,
      container = container.id
    )

    (container, item)
  }
}
