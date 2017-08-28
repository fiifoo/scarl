package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.kind.Kind.Result
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.power.ItemPowerId
import io.github.fiifoo.scarl.core.{IdSeq, Location, State}

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
                   ) extends Kind[Container] {

  def toContainer(s: State, idSeq: IdSeq, container: EntityId): Result[Item] = {
    val (item, nextIdSeq) = createItem(idSeq, container)

    Result(
      mutations = List(IdSeqMutation(nextIdSeq), NewEntityMutation(item)),
      nextIdSeq,
      item,
    )
  }

  def toLocation(s: State, idSeq: IdSeq, location: Location): Result[Container] = {
    val (containerId, containerIdSeq) = idSeq()

    val container = Container(ContainerId(containerId), location)
    val (item, nextIdSeq) = createItem(containerIdSeq, container.id)

    Result(
      mutations = List(IdSeqMutation(nextIdSeq), NewEntityMutation(container), NewEntityMutation(item)),
      nextIdSeq,
      container,
    )
  }

  private def createItem(idSeq: IdSeq, container: EntityId): (Item, IdSeq) = {
    val (nextId, nextIdSeq) = idSeq()

    val item = Item(
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

    (item, nextIdSeq)
  }
}
