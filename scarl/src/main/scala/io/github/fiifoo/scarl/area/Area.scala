package io.github.fiifoo.scarl.area

import io.github.fiifoo.scarl.area.Area.ConduitSource
import io.github.fiifoo.scarl.area.template.TemplateId
import io.github.fiifoo.scarl.area.theme.ThemeId
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.kind.ItemKindId

case class Area(id: AreaId,
                template: TemplateId,
                theme: ThemeId,
                power: Map[CombatPower.Category, (Int, Int)] = Map(),
                conduits: List[ConduitSource] = List()
               )

object Area {

  case class ConduitSource(target: AreaId,
                           sourceItem: ItemKindId,
                           targetItem: Option[ItemKindId],
                           tag: Option[String]
                          )

}
