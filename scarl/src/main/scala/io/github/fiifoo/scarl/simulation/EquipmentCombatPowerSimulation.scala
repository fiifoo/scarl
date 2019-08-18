package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.item.Equipment._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Rng

case class EquipmentCombatPowerSimulation(matches: Int = 25,
                                          turns: Int = 30,
                                          teamSize: Int = 1,
                                          teamDistance: Int = 5
                                         ) {

  val categories: List[Category] = Equipment.categories.toList

  val simulation = CombatPowerSimulation(matches, turns, teamSize, teamDistance)

  def apply(items: Iterable[ItemKind], combatants: Iterable[CreatureKind], rng: Rng = Rng(1)): CombatPower.Equipment = {
    if (items.isEmpty || combatants.isEmpty) {
      return Map()
    }

    val simulate = simulateItem(combatants, rng) _

    (categories map (category => {
      val data = items flatMap (item => {
        val equipment = category.extractEquipment(item)

        equipment filter (_.category == category) map ((item, _))
      })
      val results = (data map (x => {
        val (item, equipment) = x

        item.id -> simulate(item, equipment)
      })).toMap

      category -> normalizeResults(results)
    })).toMap
  }

  private def normalizeResults(results: Map[ItemKindId, Int]): Map[ItemKindId, Int] = {
    if (results.isEmpty) {
      return results
    }

    val min = results.values.min
    val max = results.values.max
    val diff = max - min

    if (diff == 0) {
      results transform ((_, _) => 100)
    } else {
      results transform ((_, power) => (power - min) * 100 / diff)
    }
  }

  private def simulateItem(combatants: Iterable[CreatureKind], rng: Rng)
                          (item: ItemKind, equipment: Equipment): Int = {
    val results = combatants map simulateCombat(item, equipment, rng)

    results.sum / results.size
  }

  private def simulateCombat(item: ItemKind, equipment: Equipment, rng: Rng)
                            (combatant: CreatureKind): Int = {
    val slot = equipment.slots.head

    val a = Combatant.withEquipment(combatant, Map(slot -> item))
    val b = Combatant(combatant.copy(id = CreatureKindId("_opponent")))

    val results = simulation.simulate(List(a, b), rng)

    results.average(a.id) - results.average(b.id)
  }
}
