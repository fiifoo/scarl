package models.json

import io.github.fiifoo.scarl.core.Rng.WeightedChoices
import play.api.libs.json._

object FormatUtils {
  def formatEmpty[T](v: T): Format[T] = {
    new Format[T] {
      def writes(o: T) = JsNull

      def reads(json: JsValue) = JsSuccess(v)
    }
  }

  def formatIntId[T](getValue: T => Int, getId: Int => T): Format[T] = {
    new Format[T] {
      def writes(id: T) = JsNumber(getValue(id))

      def reads(json: JsValue) = JsSuccess(getId(json.as[Int]))
    }
  }

  def formatStringId[T](getValue: T => String, getId: String => T): Format[T] = {
    new Format[T] {
      def writes(id: T) = JsString(getValue(id))

      def reads(json: JsValue) = JsSuccess(getId(json.as[String]))
    }
  }

  def formatList[T](formatValue: Format[T]): Format[List[T]] = {
    new Format[List[T]] {
      def writes(list: List[T]) = JsArray(list map formatValue.writes)

      def reads(json: JsValue) = {
        val array = json.as[JsArray].value
        val list = (array map formatValue.reads map (_.get)).toList

        JsSuccess(list)
      }
    }
  }

  def formatMap[K, V](formatKey: Format[K],
                      formatValue: Format[V]
                     ): Format[Map[K, V]] = {

    new Format[Map[K, V]] {
      def writes(m: Map[K, V]) = Json.toJson(m map (x => JsObject(Map(
        "key" -> formatKey.writes(x._1),
        "value" -> formatValue.writes(x._2)
      ))))

      def reads(json: JsValue) = {
        val map = (json.as[JsArray].value map (x => {
          val obj = x.as[JsObject].value
          val key = formatKey.reads(obj("key")).get
          val value = formatValue.reads(obj("value")).get

          (key, value)
        })).toMap

        JsSuccess(map)
      }
    }
  }

  def formatTuple2[A, B](formatA: Format[A],
                         formatB: Format[B]
                        ): Format[(A, B)] = {

    new Format[(A, B)] {
      def writes(tuple: (A, B)) = JsObject(Map(
        "a" -> formatA.writes(tuple._1),
        "b" -> formatB.writes(tuple._2)
      ))

      def reads(json: JsValue) = {
        val obj = json.as[JsObject].value
        val a = obj("a")
        val b = obj("b")

        val tuple = (formatA.reads(a).get, formatB.reads(b).get)

        JsSuccess(tuple)
      }
    }
  }

  def formatTuple3[A, B, C](formatA: Format[A],
                            formatB: Format[B],
                            formatC: Format[C]
                           ): Format[(A, B, C)] = {

    new Format[(A, B, C)] {
      def writes(tuple: (A, B, C)) = JsObject(Map(
        "a" -> formatA.writes(tuple._1),
        "b" -> formatB.writes(tuple._2),
        "c" -> formatC.writes(tuple._3)
      ))

      def reads(json: JsValue) = {
        val obj = json.as[JsObject].value
        val a = obj("a")
        val b = obj("b")
        val c = obj("c")

        val tuple = (formatA.reads(a).get, formatB.reads(b).get, formatC.reads(c).get)

        JsSuccess(tuple)
      }
    }
  }

  def formatWeightedChoices[T](formatChoice: Format[T])
                              (implicit formatWeight: Format[Int]): Format[WeightedChoices[T]] = {

    val formatWeightedChoice = formatTuple2(formatChoice, formatWeight)

    new Format[WeightedChoices[T]] {
      def writes(choices: WeightedChoices[T]) = JsArray(choices.choices.toList map formatWeightedChoice.writes)

      def reads(json: JsValue) = {
        val array = json.as[JsArray].value
        val choices = array map formatWeightedChoice.reads map (_.get)

        JsSuccess(WeightedChoices(choices.toList))
      }
    }
  }
}
