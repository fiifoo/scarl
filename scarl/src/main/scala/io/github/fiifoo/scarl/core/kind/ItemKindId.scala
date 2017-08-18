package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State

case class ItemKindId(value: String) extends KindId {
  def apply(s: State): ItemKind = s.assets.kinds.items(this)
}
