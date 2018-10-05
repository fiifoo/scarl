package io.github.fiifoo.scarl.area.theme

import io.github.fiifoo.scarl.core.assets._
import io.github.fiifoo.scarl.world.TemplateCatalogueId

case class Theme(id: ThemeId,
                 creatures: CreatureCatalogueId,
                 items: ItemCatalogueId,
                 templates: TemplateCatalogueId,
                 terrains: TerrainCatalogueId,
                 walls: WallCatalogueId,
                 widgets: WidgetCatalogueId,
                )
