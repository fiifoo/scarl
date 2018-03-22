package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.{ItemPower, _}
import io.github.fiifoo.scarl.core.kind.ItemKind.Category
import io.github.fiifoo.scarl.core.kind.Kind.Result
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, ItemFoundMutation, Mutation, NewEntityMutation}

object ItemKind {

  trait Category

  case object UtilityCategory extends Category

  val categories: Set[Category] = Set(
    UtilityCategory,
  )
}

case class ItemKind(id: ItemKindId,
                    name: String,
                    display: Char,
                    color: String,
                    category: Option[Category] = None,
                    power: Option[Int] = None,

                    concealment: Int = 0,
                    hidden: Boolean = false,
                    locked: Option[SharedKey] = None,
                    personal: Boolean = false,
                    pickable: Boolean = false,

                    armor: Option[Armor] = None,
                    door: Option[Door] = None,
                    explosive: Option[Explosive] = None,
                    key: Option[SharedKey] = None,
                    launcher: Option[MissileLauncher] = None,
                    rangedWeapon: Option[RangedWeapon] = None,
                    shield: Option[Shield] = None,
                    usable: Option[ItemPower] = None,
                    weapon: Option[Weapon] = None
                   ) extends Kind {

  def toContainer(s: State, idSeq: IdSeq, container: EntityId): Result[Item] = {
    val (item, nextIdSeq) = createItem(s, idSeq, container)

    Result(
      mutations = List(IdSeqMutation(nextIdSeq), NewEntityMutation(item)),
      nextIdSeq,
      item,
    )
  }

  def toWidget(s: State, idSeq: IdSeq, location: Location, owner: Option[SafeCreatureId]): Result[Container] = {
    val (containerId, containerIdSeq) = idSeq()

    val container = Container(ContainerId(containerId), location, owner)
    val (item, nextIdSeq) = createItem(s, containerIdSeq, container.id, owner)

    Result(
      mutations = List(
        Some(IdSeqMutation(nextIdSeq)),
        Some(NewEntityMutation(container)),
        Some(NewEntityMutation(item)),
        getItemFoundMutation(s, item, owner),
      ).flatten,
      nextIdSeq,
      container,
    )
  }

  def toLocation(s: State, idSeq: IdSeq, location: Location): Result[Container] = {
    val (containerId, containerIdSeq) = idSeq()

    val container = Container(ContainerId(containerId), location)
    val (item, nextIdSeq) = createItem(s, containerIdSeq, container.id)

    Result(
      mutations = List(IdSeqMutation(nextIdSeq), NewEntityMutation(container), NewEntityMutation(item)),
      nextIdSeq,
      container,
    )
  }

  private def createItem(s: State,
                         idSeq: IdSeq,
                         container: EntityId,
                         owner: Option[SafeCreatureId] = None
                        ): (Item, IdSeq) = {
    val (nextId, nextIdSeq) = idSeq()

    val item = Item(
      id = ItemId(nextId),
      kind = id,
      container = container,

      concealment = concealment,
      hidden = hidden,
      locked = locked,
      personal = personal,
      pickable = pickable,

      armor = armor,
      door = door,
      explosive = explosive map (explosive => {
        (owner flatMap (_ (s))) map (_.stats.explosive.add(explosive)) getOrElse explosive
      }),
      key = key,
      launcher = launcher,
      rangedWeapon = rangedWeapon,
      shield = shield,
      usable = usable,
      weapon = weapon
    )

    (item, nextIdSeq)
  }

  private def getItemFoundMutation(s: State, item: Item, owner: Option[SafeCreatureId]): Option[Mutation] = {
    if (!item.hidden) {
      return None
    }

    (owner flatMap (_ (s))) map (owner => ItemFoundMutation(item.id, owner.id))
  }
}
