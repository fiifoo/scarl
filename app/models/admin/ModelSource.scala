package models.admin

import scala.reflect.runtime.universe.Type

case class ModelSource(id: Model.Id, relationId: Model.RelationId, t: Type, dataPath: List[String], polymorphic: List[Type])

object ModelSource {
  def apply(t: Type, dataPath: List[String], polymorphic: List[Type] = List()): ModelSource = {
    ModelSource(
      id = Model.Id(t),
      relationId = Model.RelationId(t),
      t = t,
      dataPath = dataPath,
      polymorphic = polymorphic,
    )
  }
}
