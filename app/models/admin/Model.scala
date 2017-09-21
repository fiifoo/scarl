package models.admin

import models.admin.Utils.getModelKey
import play.api.libs.json._

import scala.reflect.runtime.universe.{TermName, Type}

case class Model(id: Model.Id,
                 relationId: Model.RelationId,
                 dataPath: List[String],
                 properties: List[Property] = List(),
                 polymorphic: List[SubModel.Id] = List(),
                )

object Model {

  case class Id(value: String)

  case class RelationId(value: String)

  object Id {
    def apply(t: Type): Id = Id(getModelKey(t))
  }

  object RelationId {
    def apply(t: Type): RelationId = fromProperty(t.member(TermName("id")).typeSignature)

    def fromProperty(t: Type): RelationId = RelationId(getModelKey(t))
  }

  lazy private implicit val propertyWrites = Property.writes
  lazy private implicit val subModelIdWrites = SubModel.idWrites

  lazy implicit val idWrites: Writes[Id] = id => JsString(id.value)

  lazy implicit val relationIdWrites: Writes[RelationId] = id => JsString(id.value)

  lazy val writes: Writes[Model] = Json.writes[Model]
}
