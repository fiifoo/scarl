package dal

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Files, StandardOpenOption}

import game.Simulations
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind.{ItemKind, WidgetKind}
import io.github.fiifoo.scarl.world.WorldAssets
import javax.inject.{Inject, Singleton}
import models.Data
import play.api.Environment
import play.api.libs.json.{JsValue, Json}

@Singleton
class AssetsRepository @Inject()(environment: Environment) {

  lazy private val dataFile = environment.getFile("data/data.json").toPath
  lazy private implicit val dataReads = Data.dataReads

  def build(): WorldAssets = {
    val data = read().as[Data]

    WorldAssets(
      data.areas,
      simulateCombatPower(data),
      data.communications,
      data.factions,
      data.keys,
      data.kinds,
      data.progressions,
      data.templates,
      data.themes
    )
  }

  def read(): JsValue = {
    val raw = new String(Files.readAllBytes(dataFile), UTF_8)

    Json.parse(raw)
  }

  def write(data: JsValue): Unit = {
    val formatted = Json.prettyPrint(data).replaceAll("\\r\\n?", "\n")

    Files.write(
      dataFile,
      formatted.getBytes(UTF_8),
      StandardOpenOption.CREATE,
      StandardOpenOption.TRUNCATE_EXISTING
    )
  }

  private def simulateCombatPower(data: Data): CombatPower = {
    Simulations.combatPower(data.kinds.creatures.values)
      .copy(
        equipment = getEquipmentCombatPower(data),
        item = getItemCombatPower(data),
        widget = getWidgetCombatPower(data)
      )
  }

  private def getEquipmentCombatPower(data: Data): CombatPower.Equipment = {
    val simulated = Simulations.equipmentCombatPower(data.kinds.items.values, data.kinds.creatures.values)
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

  private def getWidgetCombatPower(data: Data): CombatPower.Widget = {
    (WidgetKind.categories map (category => {
      val widgets = data.kinds.widgets filter (_._2.category contains category) flatMap (x => {
        val (id, widget) = x

        widget.power map (id -> _)
      })

      category -> widgets
    })).toMap
  }
}
