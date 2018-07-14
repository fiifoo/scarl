package models

import game.Simulate
import io.github.fiifoo.scarl.area.template.Template
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind.{CreatureKind, ItemKind, WidgetKind}
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
        equipment = getEquipmentCombatPower(data),
        item = getItemCombatPower(data),
        template = getTemplateCombatPower(data),
        widget = getWidgetCombatPower(data)
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

  private def getItemCombatPower(data: Data): CombatPower.Item = {
    (ItemKind.categories map (category => {
      val items = data.kinds.items filter (_._2.category contains category) flatMap (x => {
        val (id, item) = x

        item.power map (id -> _)
      })

      category -> items
    })).toMap
  }

  private def getTemplateCombatPower(data: Data): CombatPower.Template = {
    (Template.categories map (category => {
      val templates = data.templates filter (_._2.category contains category) flatMap (x => {
        val (id, template) = x

        template.power map (id -> _)
      })

      category -> templates
    })).toMap
  }

  private def getWidgetCombatPower(data: Data): CombatPower.Widget = {
    (WidgetKind.categories map (category => {
      val widgets = data.kinds.widgets filter (_._2.category contains category) flatMap (x => {
        val (id, widget) = x

        widget.power map (id -> _)
      })

      category -> widgets
    })).toMap
  }

  private def getCombatants(data: Data): Iterable[CreatureKind] = {
    data.kinds.creatures.values filter (_.stats.melee.damage > 0)
  }
}
