package models.admin

import play.api.libs.json.{Json, Writes}

case class Property(name: String, fieldType: FieldType)

object Property {
  lazy private implicit val fieldTypeWrites = FieldType.writes

  lazy val writes: Writes[Property] = Json.writes[Property]
}
