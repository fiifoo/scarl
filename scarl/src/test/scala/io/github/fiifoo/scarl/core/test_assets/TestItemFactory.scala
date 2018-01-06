package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}

object TestItemFactory {

  def create(id: ItemId = ItemId(0), kind: ItemKindId = ItemKindId("item"), container: EntityId): Item = {
    Item(id, kind, container)
  }

  def generate(s: State, count: Int, container: EntityId): State = {

    (0 until count).foldLeft(s)((s, _) => {
      val (nextId, nextIdSeq) = s.idSeq()
      val item = create(ItemId(nextId), container = container)

      NewEntityMutation(item)(IdSeqMutation(nextIdSeq)(s))
    })
  }

  def generate(s: State, count: Int, location: Location): State = {

    (0 until count).foldLeft(s)((s, _) => {
      val (containerId, containerIdSeq) = s.idSeq()
      val container = Container(ContainerId(containerId), location)
      val ns = NewEntityMutation(container)(s)

      val (itemId, itemIdSeq) = containerIdSeq()
      val item = create(ItemId(itemId), container = container.id)
      NewEntityMutation(item)(IdSeqMutation(itemIdSeq)(ns))
    })
  }
}
