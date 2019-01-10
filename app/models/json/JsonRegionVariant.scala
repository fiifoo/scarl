package models.json

import io.github.fiifoo.scarl.world.{RegionRequirements, RegionVariant, RegionVariantKey}
import play.api.libs.json._

object JsonRegionVariant {
  lazy private implicit val goalIdFormat = JsonGoal.goalIdFormat

  lazy private implicit val variantRequirementsFormat = Json.format[RegionRequirements]

  lazy implicit val variantKeyFormat: Format[RegionVariantKey] = Json.format[RegionVariantKey]
  lazy val variantFormat: Format[RegionVariant] = Json.format
}
