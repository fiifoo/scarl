package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.template.FixedContent.MachinerySource
import io.github.fiifoo.scarl.core.entity.{Machinery, MachineryId}
import io.github.fiifoo.scarl.core.geometry.{Location, Rotation}
import io.github.fiifoo.scarl.core.item.Mechanism
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{State, Tag}

case class FixedContent(conduitLocations: Map[Location, Option[Tag]] = Map(),
                        creatures: Map[Location, CreatureKindId] = Map(),
                        gatewayLocations: Set[Location] = Set(),
                        items: Map[Location, List[ItemKindId]] = Map(),
                        machinery: Set[MachinerySource] = Set(),
                        restrictedLocations: Set[Location] = Set(),
                        terrains: Map[Location, TerrainKindId] = Map(),
                        walls: Map[Location, WallKindId] = Map(),
                        widgets: Map[Location, WidgetKindId] = Map()
                       ) {

  def rotate(rotation: Rotation): FixedContent = FixedContent(
    conduitLocations = rotation.mapKey(this.conduitLocations),
    creatures = rotation.mapKey(this.creatures),
    gatewayLocations = this.gatewayLocations map rotation.apply,
    items = rotation.mapKey(this.items),
    machinery = rotateMachinery(rotation),
    restrictedLocations = this.restrictedLocations map rotation.apply,
    terrains = rotation.mapKey(this.terrains),
    walls = rotation.mapKey(this.walls),
    widgets = rotation.mapKey(this.widgets),
  )

  private def rotateMachinery(rotation: Rotation): Set[MachinerySource] = {
    this.machinery map (machinery => {
      machinery.copy(
        controls = machinery.controls map rotation.apply,
        targets = machinery.targets map rotation.apply
      )
    })
  }
}

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
