package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.creature.Stats._

object Stats {

  case class Melee(attack: Int = 0, damage: Int = 0) {
    def add(x: Melee): Melee = {
      copy(attack + x.attack, damage + x.damage)
    }
  }

  case class Ranged(attack: Int = 0, damage: Int = 0, range: Int = 0) {
    def add(x: Ranged): Ranged = {
      copy(attack + x.attack, damage + x.damage, range + x.range)
    }
  }

  case class Sight(range: Int = 0) {
    def add(x: Sight): Sight = {
      copy(range + x.range)
    }
  }

}

case class Stats(health: Int = 0,
                 defence: Int = 0,
                 armor: Int = 0,
                 melee: Melee = Melee(),
                 ranged: Ranged = Ranged(),
                 sight: Sight = Sight()
                ) {

  def add(x: Stats): Stats = {
    copy(
      health + x.health,
      defence + x.defence,
      armor + x.armor,
      melee.add(x.melee),
      ranged.add(x.ranged),
      sight.add(x.sight)
    )
  }
}
