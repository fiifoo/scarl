package models.save

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Files, Paths, StandardOpenOption}

import play.api.libs.json.{JsValue, Json}

class FileSaveStorage(root: String) extends SaveStorage {
  val file = Paths.get(root, "save.json")

  def load(): Option[JsValue] = {
    if (Files.exists(file)) {
      val data = new String(Files.readAllBytes(file), UTF_8)

      Some(Json.parse(data))
    } else {
      None
    }
  }

  def save(json: JsValue): Unit = {
    Files.write(
      file,
      json.toString.getBytes(UTF_8),
      StandardOpenOption.CREATE,
      StandardOpenOption.TRUNCATE_EXISTING
    )
  }

  def clear(): Unit = {
    Files.deleteIfExists(file)
  }
}
