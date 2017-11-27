package dal

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Files, StandardOpenOption}
import javax.inject.{Inject, Singleton}

import game.Simulations
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.world.WorldAssets
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
      data.kinds,
      data.progressions,
      data.templates,
      data.themes,
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

  def simulateCombatPower(data: Data): CombatPower = {
    Simulations.combatPower(data.kinds.creatures.values)
  }
}
