package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.feature.Feature
import io.github.fiifoo.scarl.area.template.ContentSelection._
import io.github.fiifoo.scarl.area.template.RandomizedContentSource.{ConduitLocations, Entrances, Routing}
import io.github.fiifoo.scarl.core.Tag
import io.github.fiifoo.scarl.core.creature.FactionId

object RandomizedContentSource {

  case class Entrances(min: Int = 0,
                       max: Int = 0,
                       door: Option[DoorSelection] = None
                      )

  case class ConduitLocations(min: Int = 0,
                              max: Int = 0,
                              tag: Option[Tag] = None
                             )

  case class Routing(strict: Boolean = false, terrain: Option[TerrainSelection] = None)

}

trait RandomizedContentSource {
  val id: TemplateId
  val owner: Option[FactionId]
  val border: Option[WallSelection]
  val fill: Option[WallSelection]
  val terrain: Option[TerrainSelection]
  val routing: Option[Routing]
  val entrances: Entrances
  val conduitLocations: ConduitLocations
  val features: List[Feature]
}
