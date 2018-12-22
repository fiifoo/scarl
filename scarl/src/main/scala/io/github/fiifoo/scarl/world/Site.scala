package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.AreaId

case class Site(id: SiteId,
                region: RegionId,
                area: AreaId,
                variants: Map[VariantKey, AreaId] = Map(),
               )
