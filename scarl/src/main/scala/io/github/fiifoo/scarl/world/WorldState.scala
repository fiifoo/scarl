package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.world.{ConduitId, GoalId}

case class WorldState(assets: WorldAssets = WorldAssets(),
                      conduits: Map[ConduitId, Conduit] = Map(),
                      goals: Set[GoalId] = Set(),
                      nextConduitId: Int = 1,
                      states: Map[SiteId, State] = Map(),
                      transports: Map[TransportId, RegionId] = Map(),
                      variants: Map[RegionId, Option[RegionVariantKey]] = Map(),
                     )
