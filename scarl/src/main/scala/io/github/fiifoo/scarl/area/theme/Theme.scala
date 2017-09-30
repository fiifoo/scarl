package io.github.fiifoo.scarl.area.theme

import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId, TerrainKindId}

case class Theme(id: ThemeId,
                 creatures: Set[CreatureKindId] = Set(),
                 items: Set[ItemKindId] = Set(),
                 terrain: TerrainKindId,
                 door: ItemKindId,
                )
