package models.json

import io.github.fiifoo.scarl.core.Distribution
import io.github.fiifoo.scarl.core.Distribution.{Binomial, Uniform}
import play.api.libs.json._

object JsonDistribution {

  import JsonBase.polymorphicTypeFormat

  lazy private implicit val binomialFormat = Json.format[Binomial]
  lazy private implicit val uniformFormat = Json.format[Uniform]

  lazy val distributionFormat: Format[Distribution] = polymorphicTypeFormat(
    data => {
      case "Binomial" => data.as[Binomial]
      case "Uniform" => data.as[Uniform]
    }, {
      case distribution: Binomial => binomialFormat.writes(distribution)
      case distribution: Uniform => uniformFormat.writes(distribution)
    }

  )
}