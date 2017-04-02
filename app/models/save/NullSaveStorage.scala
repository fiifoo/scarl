package models.save

import play.api.libs.json.JsValue

object NullSaveStorage extends SaveStorage {
  def load(): Option[JsValue] = None

  def save(json: JsValue): Unit = {}

  def clear(): Unit = {}
}
