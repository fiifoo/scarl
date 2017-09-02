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

  lazy private implicit val formatTimestamp = new Format[Timestamp] {
    def writes(ts: Timestamp) = JsString(dateFormat.format(ts))

    def reads(json: JsValue) = JsSuccess(new Timestamp(dateFormat.parse(json.as[String]).getTime))
  }

  lazy val format: Format[Game] = Json.format

  lazy val writesWithoutSave: Writes[Game] = game => format.writes(game.copy(save = None))
}
