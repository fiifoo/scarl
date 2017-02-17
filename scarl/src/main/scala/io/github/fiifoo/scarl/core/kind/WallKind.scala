package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity.{Wall, WallId}
import io.github.fiifoo.scarl.core.{Location, State}

case class WallKind(id: WallKindId,
                    name: String,
                    display: Char,
                    color: String
                   ) extends Kind {

  def apply(s: State, location: Location): Wall = {
    Wall(
      id = WallId(s.nextEntityId),
      kind = id,
      location = location)
  }
}
