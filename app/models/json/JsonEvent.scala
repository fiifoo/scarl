package models.json

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.game.event._
import play.api.libs.json._

object JsonEvent {

  import JsonBase.polymorphicTypeWrites

  lazy private implicit val locationWrites = Json.writes[Location]
  lazy private implicit val creatureIdFormat = JsonCreature.creatureIdFormat

  lazy private val explosionEventWrites = Json.writes[ExplosionEvent]
  lazy private val genericEventWrites = Json.writes[GenericEvent]
  lazy private val hitEventWrites = Json.writes[HitEvent]
  lazy private val moveMissileEventWrites = Json.writes[MoveMissileEvent]
  lazy private val shotEventWrites = Json.writes[ShotEvent]

  lazy implicit val eventWrites: Writes[Event] = polymorphicTypeWrites({
    case event: ExplosionEvent => explosionEventWrites.writes(event)
    case event: GenericEvent => genericEventWrites.writes(event)
    case event: HitEvent => hitEventWrites.writes(event)
    case event: MoveMissileEvent => moveMissileEventWrites.writes(event)
    case event: ShotEvent => shotEventWrites.writes(event)
  })
}
