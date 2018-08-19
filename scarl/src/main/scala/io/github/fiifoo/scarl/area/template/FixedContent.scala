package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.template.ContentSelection._
import io.github.fiifoo.scarl.area.template.FixedContent.MachinerySource
import io.github.fiifoo.scarl.core.entity.{Machinery, MachineryId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Mechanism
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{State, Tag}

case class FixedContent(conduitLocations: Map[Location, Option[Tag]] = Map(),
                        creatures: Map[Location, CreatureSelection] = Map(),
                        gatewayLocations: Set[Location] = Set(),
                        items: Map[Location, List[ItemSelection]] = Map(),
                        machinery: Set[MachinerySource] = Set(),
                        restrictedLocations: Set[Location] = Set(),
                        terrains: Map[Location, TerrainSelection] = Map(),
                        walls: Map[Location, WallSelection] = Map(),
                        widgets: Map[Location, WidgetSelection] = Map()
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
