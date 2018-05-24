package dal

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Files, StandardOpenOption}

import javax.inject.{Inject, Singleton}
import models.Simulations
import play.api.Environment
import play.api.libs.json.{JsValue, Json}

@Singleton
class SimulationsRepository @Inject()(environment: Environment) {

  lazy private val simulationsFile = environment.getFile("data/simulations.json").toPath
  lazy private implicit val simulationsFormat = Simulations.simulationsFormat

  def build(): Simulations = {
    read().as[Simulations]
  }

  def read(): JsValue = {
    val raw = new String(Files.readAllBytes(simulationsFile), UTF_8)

    Json.parse(raw)
  }

  def write(simulations: Simulations): Unit = {
    val json = Json.toJson(simulations).toString()

    Files.write(
      simulationsFile,
      json.getBytes(UTF_8),
      StandardOpenOption.CREATE,
      StandardOpenOption.TRUNCATE_EXISTING
    )
  }
}
