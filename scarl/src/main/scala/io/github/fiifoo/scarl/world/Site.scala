package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoice
import io.github.fiifoo.scarl.world.Site.AreaSource
import io.github.fiifoo.scarl.world.{RegionVariantKey => BaseRegionVariantKey}

object Site {
  type RegionVariantKey = BaseRegionVariantKey // alias for admin app

  case class AreaSource(priority: Option[AreaId] = None,
                        choices: List[WeightedChoice[AreaId]] = List()
                       )

}

case class Site(id: SiteId,
                name: String,
                description: Option[String] = None,
                region: RegionId,
                area: AreaSource,
                variants: Map[Site.RegionVariantKey, AreaSource] = Map(),
               )
