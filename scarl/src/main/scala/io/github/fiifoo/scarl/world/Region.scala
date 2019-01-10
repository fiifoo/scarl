package io.github.fiifoo.scarl.world

case class Region(id: RegionId,
                  world: WorldId,
                  entrances: Map[TransportCategory, Set[SiteId]] = Map(),
                  exits: Map[TransportCategory, Set[SiteId]] = Map(),
                  variants: List[RegionVariant] = List(),
                 )
