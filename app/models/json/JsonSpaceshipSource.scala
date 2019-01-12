package models.json

import io.github.fiifoo.scarl.world.system.source.{SpaceshipSource, SpaceshipSourceId}
import play.api.libs.json._

object JsonSpaceshipSource {

  import JsonBase.{mapReads, stringIdFormat}

  lazy implicit val spaceshipSourceIdFormat: Format[SpaceshipSourceId] = stringIdFormat(_.value, SpaceshipSourceId.apply)

  lazy implicit val spaceshipSourceFormat: Format[SpaceshipSource] = Json.format

  lazy val spaceshipMapReads: Reads[Map[SpaceshipSourceId, SpaceshipSource]] = mapReads
}
