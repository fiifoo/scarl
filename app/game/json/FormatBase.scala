package game.json

import io.github.fiifoo.scarl.core.Location
import play.api.libs.json._

object FormatBase {
  implicit val formatChar = new Format[Char] {
    def writes(char: Char) = JsString(char.toString)

    def reads(json: JsValue) = JsSuccess(json.as[String].toCharArray.head)
  }

  implicit val formatLocation = Json.format[Location]
}
