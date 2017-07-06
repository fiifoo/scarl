package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.core.action.Action

sealed trait InMessage

case object DebugFovQuery extends InMessage with DebugMessage

case object DebugWaypointQuery extends InMessage with DebugMessage

case class GameAction(action: Action) extends InMessage

case object InventoryQuery extends InMessage
