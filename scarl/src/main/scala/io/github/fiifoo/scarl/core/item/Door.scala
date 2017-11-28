package io.github.fiifoo.scarl.core.item

import io.github.fiifoo.scarl.core.kind.ItemKindId

case class Door(open: Boolean,
                transformTo: ItemKindId,
                hardness: Option[Int],
               )
