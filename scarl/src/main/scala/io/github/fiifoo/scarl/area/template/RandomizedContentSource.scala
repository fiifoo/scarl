package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.feature.Feature
import io.github.fiifoo.scarl.area.template.ContentSelection._
import io.github.fiifoo.scarl.area.template.RandomizedContentSource.{ConduitLocations, Entrances}
import io.github.fiifoo.scarl.core.Tag

object RandomizedContentSource {

  case class Entrances(min: Int = 0,
                       max: Int = 0,
                       door: Option[DoorSelection] = None
                      )

  case class ConduitLocations(min: Int = 0,
                              max: Int = 0,
                              tag: Option[Tag] = None
                             )

}

trait RandomizedContentSource {
  val border: Option[WallSelection]
  val fill: Option[WallSelection]
  val terrain: Option[TerrainSelection]
  val entrances: Entrances
  val conduitLocations: ConduitLocations
  val features: List[Feature]
}
