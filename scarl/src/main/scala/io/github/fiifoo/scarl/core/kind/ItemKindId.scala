package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Container

case class ItemKindId(value: String) extends KindId[Container] {
  def apply(s: State): ItemKind = s.assets.kinds.items(this)
}
