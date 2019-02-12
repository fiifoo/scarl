package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.world.system.source.SpaceshipSourceId

case class Transport(id: TransportId,
                     name: String,
                     description: Option[String] = None,
                     category: TransportCategory,
                     hub: SiteId,
                     spaceship: Option[SpaceshipSourceId],
                    )
