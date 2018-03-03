package io.github.fiifoo.scarl.core.assets

import io.github.fiifoo.scarl.core.assets.CombatPower._
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId}

object CombatPower {
  type Average = Map[CreatureKindId, Int]
  type Opposed = Map[CreatureKindId, Map[CreatureKindId, Int]]

  type Equipment = Map[Equipment.Category, Map[ItemKindId, Int]]
}

case class CombatPower(average: Average = Map(), opposed: Opposed = Map())
