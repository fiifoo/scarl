package io.github.fiifoo.scarl.area

import io.github.fiifoo.scarl.core.ConduitId
import io.github.fiifoo.scarl.core.kind.ItemKindId

case class Conduit(id: ConduitId,
                   source: AreaId,
                   target: AreaId,
                   sourceItem: ItemKindId,
                   targetItem: ItemKindId
                  )
