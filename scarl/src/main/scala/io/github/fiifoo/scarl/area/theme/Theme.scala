package io.github.fiifoo.scarl.area.theme

import io.github.fiifoo.scarl.core.assets.{CreatureCatalogueId, ItemCatalogueId, WidgetCatalogueId}
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.world.TemplateCatalogueId

case class Theme(id: ThemeId,
                 door: ItemKindId,
                 terrain: TerrainKindId,
                 wall: WallKindId,
                 templates: TemplateCatalogueId,
                 creatures: CreatureCatalogueId,
                 items: ItemCatalogueId,
                 widgets: WidgetCatalogueId,
                )
