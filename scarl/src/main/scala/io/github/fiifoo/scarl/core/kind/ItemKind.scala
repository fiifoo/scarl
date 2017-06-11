package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.power.ItemPowerId
import io.github.fiifoo.scarl.core.{Location, State}

case class ItemKind(id: ItemKindId,
                    name: String,
                    display: Char,
                    color: String,

                    armor: Option[Armor] = None,
                    door: Option[Door] = None,
                    hidden: Boolean = false,
                    pickable: Boolean = false,
                    rangedWeapon: Option[RangedWeapon] = None,
                    shield: Option[Shield] = None,
                    usable: Option[ItemPowerId] = None,
                    weapon: Option[Weapon] = None
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

      armor = armor,
      door = door,
      hidden = hidden,
      pickable = pickable,
      rangedWeapon = rangedWeapon,
      shield = shield,
      usable = usable,
      weapon = weapon
    )
  }
}
