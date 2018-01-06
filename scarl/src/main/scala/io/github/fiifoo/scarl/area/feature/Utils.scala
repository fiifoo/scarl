package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.template.FixedContent
import io.github.fiifoo.scarl.core.geometry.Location

object Utils {
  def freeLocations(content: FixedContent, locations: Set[Location]): Set[Location] = {
    locations -- content.gatewayLocations -- content.conduitLocations -- content.walls.keys
  }
}
