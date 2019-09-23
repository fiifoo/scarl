package dal

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Files, StandardOpenOption}

import javax.inject.{Inject, Singleton}
import play.api.Environment
import play.api.libs.json.{JsValue, Json}

@Singleton
class AdminUiRepository @Inject()(environment: Environment, simulationsRepository: SimulationsRepository) {

  lazy private val dataFile = environment.getFile("user/admin-ui.json").toPath

  def read(): Option[JsValue] = {
    if (Files.exists(dataFile)) {

      val raw = new String(Files.readAllBytes(dataFile), UTF_8)

      Some(Json.parse(raw))
    } else {
      None
    }
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
