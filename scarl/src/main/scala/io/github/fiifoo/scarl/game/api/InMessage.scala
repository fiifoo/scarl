package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.core.action.Action

sealed trait InMessage

case class GameAction(action: Action) extends InMessage

case class InventoryQuery() extends InMessage
