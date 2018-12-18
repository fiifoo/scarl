package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Tag
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId}
import io.github.fiifoo.scarl.world.World.ConduitSource

case class World(id: WorldId,
                 start: AreaId,
                 characters: List[CreatureKindId],
                 conduits: List[ConduitSource]
                )

object World {

  case class ConduitSource(source: AreaId,
                           target: AreaId,
                           sourceItem: ItemKindId,
                           targetItem: Option[ItemKindId],
                           tag: Option[Tag]
                          )

}
