package io.github.fiifoo.scarl.game.event

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.CreatureId

sealed trait Event

case class GenericEvent(message: String) extends Event

case class HitEvent(target: CreatureId, location: Location, message: String) extends Event

case class ShotEvent(from: Location, to: Location) extends Event
