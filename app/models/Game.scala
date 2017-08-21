package models

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.TimeZone

import play.api.libs.json._

case class Game(id: Long,
                player: String,
                save: Option[String],
                running: Boolean,
                createdAt: Timestamp,
                lastPlayedAt: Timestamp
               )

object Game {
  private val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'")
  dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))

  private implicit val formatTimestamp = new Format[Timestamp] {
    def writes(ts: Timestamp) = JsString(dateFormat.format(ts))

    def reads(json: JsValue) = JsSuccess(new Timestamp(dateFormat.parse(json.as[String]).getTime))
  }

  val format = Json.format[Game]

  val writesWithoutSave = new Writes[Game] {
    def writes(game: Game): JsValue = {
      format.writes(game.copy(save = None))
    }
  }
}
