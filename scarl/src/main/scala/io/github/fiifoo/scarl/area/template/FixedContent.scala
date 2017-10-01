package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.template.FixedContent.MachinerySource
import io.github.fiifoo.scarl.core.entity.{Machinery, MachineryId}
import io.github.fiifoo.scarl.core.item.Mechanism
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{Location, State}

case class FixedContent(conduitLocations: Set[Location] = Set(),
                        creatures: Map[Location, CreatureKindId] = Map(),
                        gatewayLocations: Set[Location] = Set(),
                        items: Map[Location, List[ItemKindId]] = Map(),
                        machinery: Set[MachinerySource] = Set(),
                        terrains: Map[Location, TerrainKindId] = Map(),
                        walls: Map[Location, WallKindId] = Map(),
                        widgets: Map[Location, WidgetKindId] = Map()
                       )

object FixedContent {

  case class MachinerySource(mechanism: Mechanism, controls: Set[Location], targets: Set[Location]) {
    def apply(s: State, offset: Location): State = {
      val (nextId, nextIdSeq) = s.idSeq()
      val id = MachineryId(nextId)

      val machinery = Machinery(
        id,
        mechanism,
        controls map offset.add,
        targets map offset.add,
      )

      NewEntityMutation(machinery)(IdSeqMutation(nextIdSeq)(s))
    }
  }

}
