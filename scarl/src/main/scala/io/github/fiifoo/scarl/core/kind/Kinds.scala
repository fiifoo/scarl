package io.github.fiifoo.scarl.core.kind

case class Kinds(creatures: Map[CreatureKindId, CreatureKind] = Map(),
                 items: Map[ItemKindId, ItemKind] = Map(),
                 terrains: Map[TerrainKindId, TerrainKind] = Map(),
                 walls: Map[WallKindId, WallKind] = Map(),
                 widgets: Map[WidgetKindId, WidgetKind] = Map()
                )
