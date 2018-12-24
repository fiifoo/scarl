package models.json

import io.github.fiifoo.scarl.world.{Variant, VariantKey, VariantRequirements}
import play.api.libs.json._

object JsonVariant {
  lazy private implicit val variantRequirementsReads = Json.reads[VariantRequirements]

  lazy implicit val variantKeyFormat: Format[VariantKey] = Json.format[VariantKey]
  lazy val variantReads: Reads[Variant] = Json.reads
}
