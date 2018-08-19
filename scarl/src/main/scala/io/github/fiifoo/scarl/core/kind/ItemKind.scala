package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.kind.ItemKind.Category
import io.github.fiifoo.scarl.core.kind.Kind.{Options, Result}
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, ItemFoundMutation, Mutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{Color, State}

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
                    color: Color,
                    category: Option[Category] = None,
                    power: Option[Int] = None,

                    concealment: Int = 0,
                    hidden: Boolean = false,
                    locked: Option[Lock.Source] = None,
                    pickable: Boolean = false,

                    armor: Option[Armor] = None,
                    door: Option[Door] = None,
                    explosive: Option[Explosive] = None,
                    key: Option[SharedKey] = None,
                    launcher: Option[MissileLauncher] = None,
                    rangedWeapon: Option[RangedWeapon] = None,
                    shield: Option[Shield] = None,
                    trap: Option[ItemPower] = None,
                    usable: Option[ItemPower] = None,
                    weapon: Option[Weapon] = None
                   ) extends Kind {

  def apply(s: State, idSeq: IdSeq, location: Location, options: Options = Options()): Result[Container] = {
    val (containerId, containerIdSeq) = idSeq()

    val container = Container(
      id = ContainerId(containerId),
      location = location,
      owner = options.owner map SafeCreatureId.apply,
      tags = options.tags
    )
    val (item, nextIdSeq) = createItem(s, containerIdSeq, container.id, options)

    Result(
      mutations = List(
        Some(IdSeqMutation(nextIdSeq)),
        Some(NewEntityMutation(container)),
        Some(NewEntityMutation(item)),
        getItemFoundMutation(s, item, options.owner)
      ).flatten,
      nextIdSeq,
      container
    )
  }

  def apply(s: State, idSeq: IdSeq, container: EntityId, options: Options): Result[Item] = {
    val (item, nextIdSeq) = createItem(s, idSeq, container, options)

    Result(
      mutations = List(
        Some(IdSeqMutation(nextIdSeq)),
        Some(NewEntityMutation(item)),
        getItemFoundMutation(s, item, options.owner)
      ).flatten,
      nextIdSeq,
      item
    )
  }

  private def createItem(s: State,
                         idSeq: IdSeq,
                         container: EntityId,
                         options: Options
                        ): (Item, IdSeq) = {
    val (nextId, nextIdSeq) = idSeq()

    val item = Item(
      id = ItemId(nextId),
      kind = id,
      container = container,
      tags = options.tags,

      concealment = concealment,
      hidden = hidden,
      locked = locked map (Lock(_, options.owner)),
      pickable = pickable,

      armor = armor,
      door = door,
      explosive = explosive map (explosive => {
        (options.owner map (_ (s))) map (_.stats.explosive.add(explosive)) getOrElse explosive
      }),
      key = key,
      launcher = launcher,
      rangedWeapon = rangedWeapon,
      shield = shield,
      trap = trap,
      usable = usable,
      weapon = weapon
    )

    (item, nextIdSeq)
  }

  private def getItemFoundMutation(s: State, item: Item, owner: Option[CreatureId]): Option[Mutation] = {
    if (!item.hidden) {
      return None
    }

    (owner map (_ (s))) map (owner => ItemFoundMutation(item.id, owner.id))
  }
}
