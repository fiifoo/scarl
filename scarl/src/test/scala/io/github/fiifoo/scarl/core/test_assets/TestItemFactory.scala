package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}

object TestItemFactory {

  def create(id: ItemId = ItemId(0), container: EntityId): Item = {
    Item(id, container)
  }

  def generate(s: State, count: Int, container: EntityId): State = {

    (0 until count).foldLeft(s)((s, i) => {
      val item = create(ItemId(s.nextEntityId), container)

      NewEntityMutation(item)(s)
    })
  }

  def generate(s: State, count: Int, location: Location): State = {

    (0 until count).foldLeft(s)((s, i) => {
      val container = Container(ContainerId(s.nextEntityId), location)
      val _s = NewEntityMutation(container)(s)

      val item = create(ItemId(_s.nextEntityId), container.id)
      NewEntityMutation(item)(_s)
    })
  }
}
