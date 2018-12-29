package io.github.fiifoo.scarl.world

case class Region(id: RegionId,
                  world: WorldId,
                  access: Map[TransportCategory, Set[SiteId]] = Map(),
                  variants: List[RegionVariant] = List(),
                 )
