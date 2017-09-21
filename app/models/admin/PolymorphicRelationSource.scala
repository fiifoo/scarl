package models.admin

import scala.reflect.runtime.universe.Type

case class PolymorphicRelationSource(id: Model.RelationId, models: List[Model.Id])

object PolymorphicRelationSource {
  def apply(t: Type, models: List[Type]): PolymorphicRelationSource = {
    PolymorphicRelationSource(
      id = Model.RelationId.fromProperty(t),
      models = models.map(Model.Id(_)),
    )
  }
}
