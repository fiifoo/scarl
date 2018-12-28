package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.core.Tag
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.world.ConduitId

case class Conduit(id: ConduitId,
                   source: SiteId,
                   target: SiteId,
                   sourceItem: ItemKindId,
                   targetItem: Option[ItemKindId],
                   tag: Option[Tag]
                  )

object Conduit {

  case class Source(source: SiteId,
                    target: SiteId,
                    sourceItem: ItemKindId,
                    targetItem: Option[ItemKindId],
                    tag: Option[Tag]
                   )

}
