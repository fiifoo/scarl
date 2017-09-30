package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.kind._

case class FixedContent(conduitLocations: Set[Location] = Set(),
                        creatures: Map[Location, CreatureKindId] = Map(),
                        gatewayLocations: Set[Location] = Set(),
                        items: Map[Location, List[ItemKindId]] = Map(),
                        terrains: Map[Location, TerrainKindId] = Map(),
                        walls: Map[Location, WallKindId] = Map(),
                        widgets: Map[Location, WidgetKindId] = Map()
                       )
