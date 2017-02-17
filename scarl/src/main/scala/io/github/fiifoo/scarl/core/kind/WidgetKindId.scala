package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State

case class WidgetKindId(value: String) extends KindId {
  def apply(s: State): WidgetKind = s.kinds.widgets(this)
}
