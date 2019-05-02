package models.admin

import models.admin.FieldType._
import models.admin.Utils._
import play.api.libs.json.{Json, Writes}

import scala.reflect.runtime.universe.{Symbol, Type, typeOf}

case class Models(main: Map[Model.Id, Model], sub: Map[SubModel.Id, SubModel])

object Models {

  def apply(): Models = {
    val results = Sources.main.values map scanModel

    Models(
      main = (results map (result => {
        val main = result._1

        main.id -> main
      })).toMap,
      sub = (results flatMap (result => {
        val subs = result._2

        subs map (sub => sub.id -> sub)
      })).toMap
    )
  }

  private def scanModel(source: ModelSource): (Model, List[SubModel]) = {
    val results = if (source.polymorphic.nonEmpty) Nil else {
      getProperties(source.t) map scanProperty(Nil)(source.t)
    }

    val properties = results map (_._1)
    val subs = results flatMap (_._2)
    val polymorphic = source.polymorphic flatMap scanSubModel(Nil)

    val model = Model(
      id = source.id,
      relationId = source.relationId,
      dataPath = source.dataPath,
      properties = properties,
      polymorphic = source.polymorphic map SubModel.Id.apply
    )

    (model, subs ::: polymorphic)
  }

  private def scanSubModel(path: List[Type])(t: Type): List[SubModel] = {
    val id = SubModel.Id(t)
    val source = Sources.sub.get(id)

    val results = if (source.exists(_.polymorphic.nonEmpty)) Nil else {
      getProperties(t) map scanProperty(path)(t)
    }

    val properties = results map (_._1)
    val subs = results flatMap (_._2)
    val polymorphic = source map (_.polymorphic flatMap scanSubModel(t :: path)) getOrElse Nil

    val model = SubModel(
      id = id,
      properties = properties,
      polymorphic = source map (_.polymorphic map SubModel.Id.apply) getOrElse Nil,
      objectPolymorphism = source exists (_.objectPolymorphism)
    )

    model :: subs ::: polymorphic
  }

  private def scanProperty(path: List[Type])(t: Type)(s: Symbol): (Property, List[SubModel]) = {
    val name = getPropertyName(s)
    val (fieldType, subs) = scanField(t :: path, s.typeSignatureIn(t))

    val property = Property(name.toString, fieldType)

    (property, subs)
  }

  private def scanField(path: List[Type], t: Type, required: Boolean = true): (FieldType, List[SubModel]) = {
    t match {
      case t if t <:< typeOf[Option[_]] => scanField(t :: path, t.typeArgs.head, required = false)

      case t if t <:< typeOf[Map[_, _]] =>
        val (keyFieldType, keySubs) = scanField(t :: path, t.typeArgs.head)
        val (valueFieldType, valueSubs) = scanField(t :: path, t.typeArgs(1))

        (MapField(keyFieldType, valueFieldType, required), keySubs ::: valueSubs)

      case t if t <:< typeOf[Iterable[_]] =>
        val (valueFieldType, subs) = scanField(t :: path, t.typeArgs.head)

        (ListField(valueFieldType, required), subs)

      case t if t =:= typeOf[Int] => (IntegerField(required, getScalarFieldAlias(t)), Nil)
      case t if t =:= typeOf[Long] => (IntegerField(required, getScalarFieldAlias(t)), Nil)
      case t if t =:= typeOf[Double] => (DecimalField(required, getScalarFieldAlias(t)), Nil)
      case t if t =:= typeOf[Float] => (DecimalField(required, getScalarFieldAlias(t)), Nil)
      case t if t =:= typeOf[Boolean] => (BooleanField(required, getScalarFieldAlias(t)), Nil)
      case t if t =:= typeOf[Char] => (CharField(required, getScalarFieldAlias(t)), Nil)
      case t if t =:= typeOf[String] => (StringField(required, getScalarFieldAlias(t)), Nil)

      case t if Sources.main.isDefinedAt(Model.RelationId.fromProperty(t)) =>
        val source = Sources.main(Model.RelationId.fromProperty(t))

        (RelationField(source.id, required), Nil)

      case t if Sources.polymorphicRelation.isDefinedAt(Model.RelationId.fromProperty(t)) =>
        val source = Sources.polymorphicRelation(Model.RelationId.fromProperty(t))

        (PolymorphicRelationField(source.models, required), Nil)

      case t if isOwnType(t) || t <:< typeOf[(_, _)] || t <:< typeOf[(_, _, _)] =>
        val id = SubModel.Id(t)
        val subs = if (path exists (_ =:= t)) Nil else scanSubModel(t :: path)(t)

        (FormField(id, required), subs)

      case _ => throw new Exception("Unknown field type " + t)
    }
  }

  private def getScalarFieldAlias(t: Type): Option[String] = {
    if (isOwnType(t)) {
      Some(getModelKey(t))
    } else {
      None
    }
  }

  lazy private implicit val modelIdWrites = Model.idWrites
  lazy private implicit val subModelIdWrites = SubModel.idWrites

  lazy private implicit val modelWrites = Model.writes
  lazy private implicit val subModelWrites = SubModel.writes

  lazy val writes: Writes[Models] = Json.writes[Models]
}
