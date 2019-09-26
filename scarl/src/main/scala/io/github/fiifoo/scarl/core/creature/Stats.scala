package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.creature.Stats._
import io.github.fiifoo.scarl.core.kind.CreatureKindId

object Stats {

  sealed trait AttackStats {
    val consumption: Consumption
    val stance: Option[Stance]
  }

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

  case class Melee(attack: Int = 0,
                   damage: Int = 0,
                   consumption: Consumption = Consumption(),
                   stance: Option[Stance] = None,
                   conditions: List[Condition] = List(),
                  ) extends AttackStats {
    def add(x: Melee): Melee = {
      copy(
        attack + x.attack,
        damage + x.damage,
        consumption add x.consumption,
        stance = stance orElse x.stance,
        conditions ::: x.conditions
      )
    }
  }

  case class Ranged(attack: Int = 0,
                    damage: Int = 0,
                    range: Int = 0,
                    consumption: Consumption = Consumption(),
                    stance: Option[Stance] = None,
                    conditions: List[Condition] = List(),
                   ) extends AttackStats {
    def add(x: Ranged): Ranged = {
      copy(
        attack + x.attack,
        damage + x.damage,
        range + x.range,
        consumption add x.consumption,
        stance = stance orElse x.stance,
        conditions ::: x.conditions
      )
    }
  }

  case class Launcher(missiles: Set[CreatureKindId] = Set(),
                      range: Int = 0,
                      stance: Option[Stance] = None,
                      consumption: Consumption = Consumption()
                     ) extends AttackStats {
    def add(x: Launcher): Launcher = {
      copy(
        missiles ++ x.missiles,
        range + x.range,
        stance = stance orElse x.stance,
        consumption add x.consumption
      )
    }
  }

  case class Explosive(attack: Int = 0,
                       damage: Int = 0,
                       radius: Int = 0,
                       blast: Int = 0,
                       conditions: List[Condition] = List(),
                      ) {
    def add(x: Explosive): Explosive = {
      copy(attack + x.attack, damage + x.damage, radius + x.radius, blast + x.blast, conditions ::: x.conditions)
    }
  }

  case class Sight(range: Int = 0, sensors: Int = 0) {
    def add(x: Sight): Sight = {
      copy(range + x.range, sensors + x.sensors)
    }
  }

  case class Skill(hacking: Int = 0) {
    def add(x: Skill): Skill = {
      copy(hacking + x.hacking)
    }
  }

  case class Consumption(energy: Double = 0, materials: Double = 0) {
    def add(x: Consumption): Consumption = {
      copy(energy + x.energy, materials + x.materials)
    }

    def nonEmpty: Boolean = energy > 0 || materials > 0
  }

}

case class Stats(speed: Int = 0,
                 health: Health = Health(),
                 energy: Energy = Energy(),
                 materials: Materials = Materials(),
                 defence: Int = 0,
                 armor: Int = 0,
                 resistance: Int = 0,
                 melee: Melee = Melee(),
                 ranged: Ranged = Ranged(),
                 launcher: Launcher = Launcher(),
                 explosive: Explosive = Explosive(),
                 sight: Sight = Sight(),
                 skill: Skill = Skill()
                ) {

  def add(x: Stats): Stats = {
    copy(
      speed + x.speed,
      health add x.health,
      energy add x.energy,
      materials add x.materials,
      defence + x.defence,
      armor + x.armor,
      resistance + x.resistance,
      melee add x.melee,
      ranged add x.ranged,
      launcher add x.launcher,
      explosive add x.explosive,
      sight add x.sight,
      skill add x.skill
    )
  }
}
