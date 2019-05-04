package io.github.fiifoo.scarl.game.event

import io.github.fiifoo.scarl.core.communication.Communication
import io.github.fiifoo.scarl.core.entity.{CreatureId, Signal, UsableId}
import io.github.fiifoo.scarl.core.geometry.Location

sealed trait Event

case class GenericEvent(message: String) extends Event

case class CommunicationEvent(source: UsableId, message: String, choices: List[Communication.Choice]) extends Event

case class ExplosionEvent(location: Location) extends Event

case class HitEvent(target: CreatureId, location: Location, message: String) extends Event

case class MoveMissileEvent(from: Location, to: Location) extends Event

case class ShotEvent(from: Location, to: Location) extends Event

case class SignalEvent(signal: Signal) extends Event
