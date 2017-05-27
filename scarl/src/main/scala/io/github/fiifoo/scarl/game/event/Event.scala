package io.github.fiifoo.scarl.game.event

sealed trait Event

case class GenericEvent(message: String) extends Event
