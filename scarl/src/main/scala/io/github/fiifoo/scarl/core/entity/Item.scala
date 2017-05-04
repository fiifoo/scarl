package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.equipment._
import io.github.fiifoo.scarl.core.kind.ItemKindId

case class Item(id: ItemId,
                kind: ItemKindId,
                container: EntityId,
                pickable: Boolean = false,
                armor: Option[Armor] = None,
                rangedWeapon: Option[RangedWeapon] = None,
                shield: Option[Shield] = None,
                weapon: Option[Weapon] = None
               ) extends Entity
