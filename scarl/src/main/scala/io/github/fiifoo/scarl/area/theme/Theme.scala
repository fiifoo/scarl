package io.github.fiifoo.scarl.area.theme

import io.github.fiifoo.scarl.area.template.TemplateId
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId, TerrainKindId, WidgetKindId}
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoice

case class Theme(id: ThemeId,
                 door: ItemKindId,
                 terrain: TerrainKindId,
                 templates: List[WeightedChoice[TemplateId]] = List(),
                 creatures: List[WeightedChoice[CreatureKindId]] = List(),
                 items: List[WeightedChoice[ItemKindId]] = List(),
                 widgets: List[WeightedChoice[WidgetKindId]] = List(),
                )
