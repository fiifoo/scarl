package models.json

import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId, Message}
import play.api.libs.json._

object JsonCommunication {

  import JsonBase.{mapReads, polymorphicTypeReads, stringIdFormat}

  lazy private implicit val messageReads = Json.reads[Message]

  lazy implicit val communicationIdFormat: Format[CommunicationId] = stringIdFormat(_.value, CommunicationId.apply)

  lazy implicit val communicationReads: Reads[Communication] = polymorphicTypeReads(data => {
    case "Message" => data.as[Message]
  })

  lazy val communicationMapReads: Reads[Map[CommunicationId, Communication]] = mapReads
}
