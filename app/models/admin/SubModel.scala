package models.admin

import models.admin.Utils.getModelKey
import play.api.libs.json._

import scala.reflect.runtime.universe.Type


case class SubModel(id: SubModel.Id,
                    properties: List[Property] = List(),
                    polymorphic: List[SubModel.Id] = List(),
                    objectPolymorphism: Boolean = false,
                   )

object SubModel {

  case class Id(value: String)

  object Id {
    def apply(t: Type): Id = Id(getModelKey(t))
  }

  lazy private implicit val propertyWrites = Property.writes

  lazy implicit val idWrites: Writes[Id] = id => JsString(id.value)

  lazy val writes: Writes[SubModel] = Json.writes[SubModel]
}
