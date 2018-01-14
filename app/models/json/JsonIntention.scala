package models.json

import io.github.fiifoo.scarl.ai.intention._
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.geometry.Location
import play.api.libs.json._

object JsonIntention {

  import JsonBase.polymorphicTypeFormat

  lazy private implicit val locationFormat = Json.format[Location]
  lazy private implicit val safeCreatureIdFormat = JsonCreature.safeCreatureIdFormat

  lazy private implicit val attackFormat = Json.format[AttackIntention]
  lazy private implicit val chargeFormat = Json.format[ChargeIntention]
  lazy private implicit val followFormat = Json.format[FollowIntention]
  lazy private implicit val pursueFormat = Json.format[PursueIntention]
  lazy private implicit val travelFormat = Json.format[TravelIntention]

  lazy val intentionFormat: Format[Intention] = polymorphicTypeFormat(
    data => {
      case "FollowerIntention" => FollowerIntention
      case "GreetIntention" => GreetIntention
      case "SeekEnemyIntention" => SeekEnemyIntention

      case "AttackIntention" => data.as[AttackIntention]
      case "ChargeIntention" => data.as[ChargeIntention]
      case "FollowIntention" => data.as[FollowIntention]
      case "PursueIntention" => data.as[PursueIntention]
      case "TravelIntention" => data.as[TravelIntention]

    }, {
      case FollowerIntention => JsNull
      case GreetIntention => JsNull
      case SeekEnemyIntention => JsNull

      case intention: AttackIntention => attackFormat.writes(intention)
      case intention: ChargeIntention => chargeFormat.writes(intention)
      case intention: FollowIntention => followFormat.writes(intention)
      case intention: PursueIntention => pursueFormat.writes(intention)
      case intention: TravelIntention => travelFormat.writes(intention)
    }
  )
}
