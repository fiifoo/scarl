package models.json

import io.github.fiifoo.scarl.core.math.Rng.{WeightedChoice, WeightedChoices}
import play.api.libs.json._

object JsonBase {
  lazy val charFormat: Format[Char] = new Format[Char] {
    def writes(o: Char): JsValue = JsString(o.toString)

    def reads(json: JsValue): JsResult[Char] = JsSuccess(json.as[String].toCharArray.head)
  }

  def emptyFormat[T](emptyValue: T): Format[T] = {
    new Format[T] {
      def writes(o: T): JsValue = JsNull

      def reads(json: JsValue): JsResult[T] = JsSuccess(emptyValue)
    }
  }

  def integerIdFormat[T](getValue: T => Int, getId: Int => T): Format[T] = {
    new Format[T] {
      def writes(id: T): JsValue = JsNumber(getValue(id))

      def reads(json: JsValue): JsResult[T] = JsSuccess(getId(json.as[Int]))
    }
  }

  implicit def mapFormat[K, V](implicit keyReads: Reads[K],
                               valueReads: Reads[V],
                               keyWrites: Writes[K],
                               valueWrites: Writes[V],
                              ): Format[Map[K, V]] = {
    val _reads = mapReads[K, V]
    val _writes = mapWrites[K, V]

    new Format[Map[K, V]] {
      def writes(o: Map[K, V]): JsValue = _writes.writes(o)

      def reads(json: JsValue): JsResult[Map[K, V]] = _reads.reads(json)
    }
  }

  implicit def mapReads[K, V](implicit keyReads: Reads[K], valueReads: Reads[V]): Reads[Map[K, V]] = {
    json => JsSuccess(json.as[JsArray].as[List[(K, V)]].toMap)
  }

  implicit def mapWrites[K, V](implicit keyWrites: Writes[K], valueWrites: Writes[V]): Writes[Map[K, V]] = {
    value => Json.toJson(value.toList)
  }

  implicit def optionReads[T](implicit valueReads: Reads[T]): Reads[Option[T]] = {
    case JsNull => JsSuccess(None)
    case json: JsValue => valueReads.reads(json) map Some.apply
  }

  implicit def optionFormat[T](implicit valueReads: Reads[T], valueWrites: Writes[T]): Format[Option[T]] = {
    new Format[Option[T]] {
      def writes(option: Option[T]): JsValue = option match {
        case None => JsNull
        case Some(x) => valueWrites.writes(x)
      }

      def reads(json: JsValue): JsResult[Option[T]] = json match {
        case JsNull => JsSuccess(None)
        case json: JsValue => valueReads.reads(json) map Some.apply
      }
    }
  }

  def stringIdFormat[T](getValue: T => String, getId: String => T): Format[T] = {
    new Format[T] {
      def writes(id: T): JsValue = JsString(getValue(id))

      def reads(json: JsValue): JsResult[T] = JsSuccess(getId(json.as[String]))
    }
  }

  def polymorphicIdFormat[T, V](read: V => String => T, write: T => JsValue)
                               (implicit valueReads: Reads[V]): Format[T] = new Format[T] {
    val typeWrites = polymorphicIdWrites(write)
    val typeReads = polymorphicIdReads(read)

    def writes(o: T): JsValue = typeWrites.writes(o)

    def reads(json: JsValue): JsResult[T] = typeReads.reads(json)
  }

  def polymorphicIdReads[T, V](read: V => String => T)(implicit valueReads: Reads[V]): Reads[T] = {
    json => {
      val obj = json.as[JsObject].value
      val name = obj("type").as[String]
      val value = obj("value")

      JsSuccess(read(value.as[V])(name))
    }
  }

  def polymorphicIdWrites[T](write: T => JsValue): Writes[T] = {
    o => {
      JsObject(Map(
        "type" -> JsString(getTypeName(o)),
        "value" -> write(o),
      ))
    }
  }

  def polymorphicObjectFormat[T](read: String => T): Format[T] = new Format[T] {
    val objectWrites = polymorphicObjectWrites
    val objectReads = polymorphicObjectReads(read)

    def writes(o: T): JsValue = objectWrites.writes(o)

    def reads(json: JsValue): JsResult[T] = objectReads.reads(json)
  }

  def polymorphicObjectReads[T](read: String => T): Reads[T] = json => JsSuccess(read(json.as[String]))

  def polymorphicObjectWrites[T]: Writes[T] = o => JsString(getTypeName(o))

  def polymorphicTypeFormat[T](read: JsValue => String => T, write: T => JsValue): Format[T] = new Format[T] {
    val typeWrites = polymorphicTypeWrites(write)
    val typeReads = polymorphicTypeReads(read)

    def writes(o: T): JsValue = typeWrites.writes(o)

    def reads(json: JsValue): JsResult[T] = typeReads.reads(json)
  }

  def polymorphicTypeReads[T](read: JsValue => String => T): Reads[T] = {
    json => {
      val obj = json.as[JsObject].value
      val name = obj("type").as[String]
      val data = obj("data")

      JsSuccess(read(data)(name))
    }
  }

  def polymorphicTypeWrites[T](write: T => JsValue): Writes[T] = {
    o => {
      JsObject(Map(
        "type" -> JsString(getTypeName(o)),
        "data" -> write(o),
      ))
    }
  }

  implicit def tuple2Format[T1, T2](implicit format1: Format[T1],
                                    format2: Format[T2]
                                   ): Format[(T1, T2)] = {

    new Format[(T1, T2)] {
      def writes(tuple: (T1, T2)) = JsObject(Map(
        "_1" -> format1.writes(tuple._1),
        "_2" -> format2.writes(tuple._2),
      ))

      def reads(json: JsValue) = {
        val obj = json.as[JsObject].value
        val value1 = obj("_1")
        val value2 = obj("_2")

        JsSuccess((
          format1.reads(value1).get,
          format2.reads(value2).get,
        ))
      }
    }
  }

  implicit def tuple3Format[T1, T2, T3](implicit format1: Format[T1],
                                        format2: Format[T2],
                                        format3: Format[T3]
                                       ): Format[(T1, T2, T3)] = {

    new Format[(T1, T2, T3)] {
      def writes(tuple: (T1, T2, T3)) = JsObject(Map(
        "_1" -> format1.writes(tuple._1),
        "_2" -> format2.writes(tuple._2),
        "_3" -> format3.writes(tuple._3)
      ))

      def reads(json: JsValue) = {
        val obj = json.as[JsObject].value
        val value1 = obj("_1")
        val value2 = obj("_2")
        val value3 = obj("_3")

        JsSuccess((
          format1.reads(value1).get,
          format2.reads(value2).get,
          format3.reads(value3).get
        ))
      }
    }
  }

  implicit def weightedChoiceFormat[T](implicit valueFormat: Format[T]): Format[WeightedChoice[T]] = {
    val choiceFormat = Json.format[WeightedChoice[T]]

    new Format[WeightedChoice[T]] {
      def reads(json: JsValue): JsResult[WeightedChoice[T]] = choiceFormat.reads(json)

      def writes(o: WeightedChoice[T]): JsValue = choiceFormat.writes(o)
    }
  }

  implicit def weightedChoicesFormat[T](implicit valueFormat: Format[T]): Format[WeightedChoices[T]] = {
    implicit val choiceFormat = Json.format[WeightedChoice[T]]
    val choicesFormat = Json.format[WeightedChoices[T]]

    new Format[WeightedChoices[T]] {
      def reads(json: JsValue): JsResult[WeightedChoices[T]] = choicesFormat.reads(json)

      def writes(o: WeightedChoices[T]): JsValue = choicesFormat.writes(o)
    }
  }

  implicit def weightedChoicesReads[T](implicit valueReads: Reads[T]): Reads[WeightedChoices[T]] = {
    implicit val choiceReads = Json.reads[WeightedChoice[T]]
    val choicesReads = Json.reads[WeightedChoices[T]]

    json => choicesReads.reads(json)
  }

  implicit def weightedChoicesWrites[T](implicit valueWrites: Writes[T]): Writes[WeightedChoices[T]] = {
    implicit val choiceWrites = Json.writes[WeightedChoice[T]]
    val choicesWrites = Json.writes[WeightedChoices[T]]

    json => choicesWrites.writes(json)
  }

  private def getTypeName[T](t: T): String = t.getClass.getCanonicalName
    .replace("$", "")
    .split('.')
    .filter(_.matches("^[A-Z].*"))
    .mkString(".")
}
