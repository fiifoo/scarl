package models.save

import play.api.libs.json.JsValue

trait SaveStorage {
  def load(): Option[JsValue]

  def save(json: JsValue): Unit

  def clear(): Unit
}
