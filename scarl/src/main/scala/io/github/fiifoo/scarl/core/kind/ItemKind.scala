package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.{Location, State}

case class ItemKind(id: ItemKindId,
                    name: String,
                    display: Char,
                    color: String,
                    pickable: Boolean = false,
                    armor: Option[Armor] = None,
                    rangedWeapon: Option[RangedWeapon] = None,
                    shield: Option[Shield] = None,
                    weapon: Option[Weapon] = None,
                    door: Option[Door] = None
                   ) extends Kind {

  def apply(s: State, container: EntityId): Item = {
    createItem(s.nextEntityId, container)
  }

  def apply(s: State, location: Location): (Container, Item) = {
    val container = Container(ContainerId(s.nextEntityId), location)
    val item = createItem(s.nextEntityId + 1, container.id)

    (container, item)
  }

  private def createItem(nextId: Int, container: EntityId): Item = {
    Item(
      id = ItemId(nextId),
      kind = id,
      container = container,
      pickable = pickable,
      armor = armor,
      rangedWeapon = rangedWeapon,
      shield = shield,
      weapon = weapon,
      door = door
    )
  }
}
