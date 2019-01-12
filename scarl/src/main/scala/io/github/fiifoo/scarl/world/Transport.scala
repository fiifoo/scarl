package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.world.system.source.SpaceshipSourceId

case class Transport(id: TransportId,
                     category: TransportCategory,
                     hub: SiteId,
                     spaceship: Option[SpaceshipSourceId],
                    )
