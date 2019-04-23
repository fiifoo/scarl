package models.json

import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId}
import play.api.libs.json._

object JsonCommunication {

  import JsonBase.{mapReads, stringIdFormat}

  lazy implicit val communicationIdFormat: Format[CommunicationId] = stringIdFormat(_.value, CommunicationId.apply)

  lazy implicit val communicationReads: Reads[Communication] = Json.reads

  lazy val communicationMapReads: Reads[Map[CommunicationId, Communication]] = mapReads
}
