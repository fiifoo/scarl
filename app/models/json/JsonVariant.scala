package models.json

import io.github.fiifoo.scarl.world.{Variant, VariantKey, VariantRequirements}
import play.api.libs.json._

object JsonVariant {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val variantRequirementsReads = Json.reads[VariantRequirements]

  lazy implicit val variantKeyFormat: Format[VariantKey] = stringIdFormat(_.value, VariantKey.apply)
  lazy implicit val variantReads: Reads[Variant] = Json.reads

  lazy val variantMapReads: Reads[Map[VariantKey, Variant]] = mapReads
}
