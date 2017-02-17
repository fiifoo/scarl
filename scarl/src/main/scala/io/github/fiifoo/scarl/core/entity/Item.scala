package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.kind.ItemKindId

case class Item(id: ItemId,
                kind: ItemKindId,
                container: EntityId
               ) extends Entity
