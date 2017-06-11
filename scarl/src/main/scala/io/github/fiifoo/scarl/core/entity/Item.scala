package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.power.ItemPowerId

case class Item(id: ItemId,
                kind: ItemKindId,
                container: EntityId,

                armor: Option[Armor] = None,
                door: Option[Door] = None,
                hidden: Boolean = false,
                pickable: Boolean = false,
                rangedWeapon: Option[RangedWeapon] = None,
                shield: Option[Shield] = None,
                usable: Option[ItemPowerId] = None,
                weapon: Option[Weapon] = None
               ) extends Entity
