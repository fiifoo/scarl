package io.github.fiifoo.scarl.world.system.source

import io.github.fiifoo.scarl.core.Color

case class SpaceshipSource(id: SpaceshipSourceId,
                           name: String,
                           description: Option[String],
                           color: Color,
                           acceleration: Double,
                          )
