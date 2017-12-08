package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.creature.Stats._
import io.github.fiifoo.scarl.core.kind.CreatureKindId

object Stats {

  case class Health(max: Int = 0, regen: Double = 0) {
    def add(x: Health): Health = {
      copy(max + x.max, regen + x.regen)
    }
  }

  case class Energy(max: Int = 0, regen: Double = 0) {
    def add(x: Energy): Energy = {
      copy(max + x.max, regen + x.regen)
    }
  }

  case class Materials(max: Int = 0, regen: Double = 0) {
    def add(x: Materials): Materials = {
      copy(max + x.max, regen + x.regen)
    }
  }

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

  case class Explosive(attack: Int = 0, damage: Int = 0, radius: Int = 0, blast: Int = 0) {
    def add(x: Explosive): Explosive = {
      copy(attack + x.attack, damage + x.damage, radius + x.radius, blast + x.blast)
    }
  }

  case class Launcher(missile: Option[CreatureKindId] = None, range: Int = 0) {
    def add(x: Launcher): Launcher = {
      copy(x.missile.orElse(missile), range + x.range)
    }
  }

  case class Sight(range: Int = 0) {
    def add(x: Sight): Sight = {
      copy(range + x.range)
    }
  }

}

case class Stats(speed: Double = 1,
                 health: Health = Health(),
                 energy: Energy = Energy(),
                 materials: Materials = Materials(),
                 defence: Int = 0,
                 armor: Int = 0,
                 melee: Melee = Melee(),
                 ranged: Ranged = Ranged(),
                 launcher: Launcher = Launcher(),
                 explosive: Explosive = Explosive(),
                 sight: Sight = Sight()
                ) {

  def add(x: Stats): Stats = {
    copy(
      speed * x.speed,
      health.add(x.health),
      energy.add(x.energy),
      materials.add(x.materials),
      defence + x.defence,
      armor + x.armor,
      melee.add(x.melee),
      ranged.add(x.ranged),
      launcher.add(x.launcher),
      explosive.add(x.explosive),
      sight.add(x.sight)
    )
  }
}
