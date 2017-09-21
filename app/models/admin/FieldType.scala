package models.admin

import play.api.libs.json._

sealed trait FieldType {
  val required: Boolean
}

object FieldType {

  case class IntegerField(required: Boolean = true) extends FieldType

  case class DecimalField(required: Boolean = true) extends FieldType

  case class BooleanField(required: Boolean = true) extends FieldType

  case class CharField(required: Boolean = true) extends FieldType

  case class StringField(required: Boolean = true) extends FieldType

  case class RelationField(model: Model.Id, required: Boolean = true) extends FieldType

  case class PolymorphicRelationField(models: List[Model.Id], required: Boolean = true) extends FieldType

  case class FormField(model: SubModel.Id, required: Boolean = true) extends FieldType

  case class ListField(value: FieldType, required: Boolean = true) extends FieldType

  case class MapField(key: FieldType, value: FieldType, required: Boolean = true) extends FieldType

  implicit lazy val writes = new Writes[FieldType] {
    def writes(fieldType: FieldType): JsValue = {
      val data: JsValue = fieldType match {
        case fieldType: IntegerField => integerFieldWrites.writes(fieldType)
        case fieldType: DecimalField => decimalFieldWrites.writes(fieldType)
        case fieldType: BooleanField => booleanFieldWrites.writes(fieldType)
        case fieldType: CharField => charFieldWrites.writes(fieldType)
        case fieldType: StringField => stringFieldWrites.writes(fieldType)
        case fieldType: RelationField => relationFieldWrites.writes(fieldType)
        case fieldType: PolymorphicRelationField => polymorphicRelationFieldWrites.writes(fieldType)
        case fieldType: FormField => formFieldWrites.writes(fieldType)
        case fieldType: ListField => listFieldWrites.writes(fieldType)
        case fieldType: MapField => mapFieldWrites.writes(fieldType)
      }

      JsObject(Map(
        "type" -> JsString(fieldTypeName(fieldType)),
        "data" -> data
      ))
    }
  }

  private implicit lazy val modelIdWrites = Model.idWrites
  private implicit lazy val subModelIdWrites = SubModel.idWrites

  private lazy val integerFieldWrites: Writes[IntegerField] = Json.writes[IntegerField]
  private lazy val decimalFieldWrites: Writes[DecimalField] = Json.writes[DecimalField]
  private lazy val booleanFieldWrites: Writes[BooleanField] = Json.writes[BooleanField]
  private lazy val charFieldWrites: Writes[CharField] = Json.writes[CharField]
  private lazy val stringFieldWrites: Writes[StringField] = Json.writes[StringField]
  private lazy val relationFieldWrites: Writes[RelationField] = Json.writes[RelationField]
  private lazy val polymorphicRelationFieldWrites: Writes[PolymorphicRelationField] = Json.writes[PolymorphicRelationField]
  private lazy val formFieldWrites: Writes[FormField] = Json.writes[FormField]
  private lazy val listFieldWrites: Writes[ListField] = Json.writes[ListField]
  private lazy val mapFieldWrites: Writes[MapField] = Json.writes[MapField]

  private def fieldTypeName(fieldType: FieldType): String = {
    fieldType.getClass.getSimpleName.replace("$", "")
  }
}
