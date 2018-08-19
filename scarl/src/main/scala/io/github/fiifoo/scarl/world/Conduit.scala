package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Tag
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.world.ConduitId

case class Conduit(id: ConduitId,
                   source: AreaId,
                   target: AreaId,
                   sourceItem: ItemKindId,
                   targetItem: Option[ItemKindId],
                   tag: Option[Tag]
                  )
