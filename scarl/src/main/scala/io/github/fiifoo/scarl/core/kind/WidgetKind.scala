package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity.{Container, Item, Status}
import io.github.fiifoo.scarl.core.{Location, State}

trait WidgetKind extends Kind {
  val id: WidgetKindId

  def apply(s: State, location: Location): (Container, Item, Status)
}
