package models.admin

import scala.reflect.runtime.universe.Type

case class SubModelSource(id: SubModel.Id, polymorphic: List[Type], objectPolymorphism: Boolean)

object SubModelSource {
  def apply(t: Type, polymorphic: List[Type] = List(), objectPolymorphism: Boolean = false): SubModelSource = {
    SubModelSource(
      id = SubModel.Id(t),
      polymorphic = polymorphic,
      objectPolymorphism = objectPolymorphism,
    )
  }
}
