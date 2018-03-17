package io.github.fiifoo.scarl.area.theme

import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId, TerrainKindId, WidgetKindId}

case class Theme(id: ThemeId,
                 creatures: Set[CreatureKindId] = Set(),
                 items: Set[ItemKindId] = Set(),
                 widgets: Set[WidgetKindId] = Set(),
                 terrain: TerrainKindId,
                 door: ItemKindId,
                )
