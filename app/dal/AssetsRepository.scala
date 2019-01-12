package dal

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Files, StandardOpenOption}

import io.github.fiifoo.scarl.world.WorldAssets
import javax.inject.{Inject, Singleton}
import models.Data
import play.api.Environment
import play.api.libs.json.{JsValue, Json}

@Singleton
class AssetsRepository @Inject()(environment: Environment, simulationsRepository: SimulationsRepository) {

  lazy private val dataFile = environment.getFile("data/data.json").toPath
  lazy private implicit val dataReads = Data.dataReads

  def build(): WorldAssets = {
    val data = readObject()

    val simulations = simulationsRepository.build()

    WorldAssets(
      data.areas,
      data.catalogues,
      simulations.combatPower,
      data.communications,
      data.factions,
      data.goals,
      data.keys,
      data.kinds,
      data.progressions,
      data.recipes,
      data.regions,
      data.sites,
      data.spaceships,
      data.stellarBodies,
      data.templates,
      data.themes,
      data.transports,
      data.worlds
    )
  }

  def readObject(): Data = {
    read().as[Data]
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
}
