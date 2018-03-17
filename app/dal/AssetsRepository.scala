package dal

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Files, StandardOpenOption}

import game.Simulations
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
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
    val result = Simulations.combatPower(data.kinds.creatures.values)
    val equipment = getEquipmentCombatPower(data)

    result.copy(equipment = equipment)
  }

  private def getEquipmentCombatPower(data: Data): CombatPower.Equipment = {
    val simulated = simulateEquipmentCombatPower(data)
    val fixed = data.kinds.items flatMap (x => {
      val (id, item) = x

      item.combatPower map (id -> _)
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

  private def simulateEquipmentCombatPower(data: Data): CombatPower.Equipment = {
    Simulations.equipmentCombatPower(data.kinds.items.values, data.kinds.creatures.values)
  }
}
