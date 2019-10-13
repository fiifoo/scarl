package models

import game.Simulate
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind.CreatureKind
import models.json.JsonCombatPower
import play.api.libs.json.{Format, Json}

case class Simulations(combatPower: CombatPower)

object Simulations {
  def apply(data: Data): Simulations = Simulations(
    simulateCombatPower(data)
  )

  lazy private implicit val combatPowerFormat = JsonCombatPower.combatPowerFormat
  lazy val simulationsFormat: Format[Simulations] = Json.format[Simulations]

  private def simulateCombatPower(data: Data): CombatPower = {
    Simulate.combatPower(getCombatants(data))
      .copy(
        equipment = getEquipmentCombatPower(data)
      )
  }

  private def getEquipmentCombatPower(data: Data): CombatPower.Equipment = {
    val simulated = Simulate.equipmentCombatPower(data.kinds.items.values, getCombatants(data))
    val fixed = data.kinds.items flatMap (x => {
      val (id, item) = x

      item.power map (id -> _)
    })

    (Equipment.categories foldLeft simulated) ((result, category) => {
      val items = fixed filter (x => {
        val (id, _) = x
        val item = data.kinds.items(id)

        category.extractEquipment(item).isDefined
      })

      result + (category -> (result(category) ++ items))
    })
  }

  private def getCombatants(data: Data): Iterable[CreatureKind] = {
    data.kinds.creatures.values filter (x => x.stats.melee.damage > 0 || x.stats.ranged.damage > 0)
  }
}
