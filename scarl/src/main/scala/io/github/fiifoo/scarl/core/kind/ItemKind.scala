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
    Item(
      id = ItemId(s.nextEntityId),
      kind = id,
      container = container
    )
  }

  def apply(s: State, location: Location): (Container, Item) = {
    val container = Container(ContainerId(s.nextEntityId), location)

    val item = Item(
      id = ItemId(s.nextEntityId + 1),
      kind = id,
      container = container.id,
      pickable = pickable,
      armor = armor,
      rangedWeapon = rangedWeapon,
      shield = shield,
      weapon = weapon,
      door = door
    )

    (container, item)
  }
}
