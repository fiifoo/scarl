package io.github.fiifoo.scarl.world

case class Region(id: RegionId,
                  world: WorldId,
                  variants: Map[VariantKey, Variant] = Map()
                 )
