package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.world.{RegionVariantKey => BaseRegionVariantKey}

object Site {
  type RegionVariantKey = BaseRegionVariantKey // alias for admin app
}

case class Site(id: SiteId,
                region: RegionId,
                area: AreaId,
                variants: Map[Site.RegionVariantKey, AreaId] = Map(),
               )
