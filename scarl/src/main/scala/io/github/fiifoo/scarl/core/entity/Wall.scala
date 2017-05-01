package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.kind.WallKindId

case class Wall(id: WallId,
                kind: WallKindId,
                location: Location
               ) extends Entity with Locatable {

  def setLocation(location: Location): Wall = copy(location = location)
}
