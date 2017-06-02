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

  case class Explosive(attack: Int = 0, damage: Int = 0, radius: Int = 0) {
    def add(x: Explosive): Explosive = {
      copy(attack + x.attack, damage + x.damage, radius + x.radius)
    }
  }

  case class Sight(range: Int = 0) {
    def add(x: Sight): Sight = {
      copy(range + x.range)
    }
  }

}

case class Stats(speed: Double = 1,
                 health: Int = 0,
                 defence: Int = 0,
                 armor: Int = 0,
                 melee: Melee = Melee(),
                 ranged: Ranged = Ranged(),
                 explosive: Explosive = Explosive(),
                 sight: Sight = Sight()
                ) {

  def add(x: Stats): Stats = {
    copy(
      speed * x.speed,
      health + x.health,
      defence + x.defence,
      armor + x.armor,
      melee.add(x.melee),
      ranged.add(x.ranged),
      explosive.add(x.explosive),
      sight.add(x.sight)
    )
  }
}
