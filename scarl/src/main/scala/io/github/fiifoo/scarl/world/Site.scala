package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.AreaId

object Site {
    type RegionVariantKey = VariantKey // alias for admin app
}

case class Site(id: SiteId,
                region: RegionId,
                area: AreaId,
                variants: Map[Site.RegionVariantKey, AreaId] = Map(),
               )
