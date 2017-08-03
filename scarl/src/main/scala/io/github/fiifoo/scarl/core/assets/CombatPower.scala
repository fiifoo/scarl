package io.github.fiifoo.scarl.core.assets

import io.github.fiifoo.scarl.core.assets.CombatPower._
import io.github.fiifoo.scarl.core.kind.CreatureKindId

object CombatPower {
  type Average = Map[CreatureKindId, Int]
  type Opposed = Map[CreatureKindId, Map[CreatureKindId, Int]]
}

case class CombatPower(average: Average = Map(), opposed: Opposed = Map())
