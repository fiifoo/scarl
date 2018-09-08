package io.github.fiifoo.scarl.game.player

import io.github.fiifoo.scarl.core.kind.ItemKindId

case class Settings(quickItems: Map[Int, ItemKindId] = Map()) {

  def setQuickItem(slot: Int, item: Option[ItemKindId]): Settings = {
    val next = item map (item => {
      (this.quickItems filter (_._2 != item)) + (slot -> item)
    }) getOrElse {
      this.quickItems - slot
    }

    this.copy(quickItems = next)
  }
}
