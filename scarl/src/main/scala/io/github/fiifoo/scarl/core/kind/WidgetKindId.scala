package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Container

case class WidgetKindId(value: String) extends KindId[Container] {
  def apply(s: State): WidgetKind = s.assets.kinds.widgets(this)
}
