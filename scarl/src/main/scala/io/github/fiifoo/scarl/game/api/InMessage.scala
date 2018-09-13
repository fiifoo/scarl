package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.action.EquipWeaponsAction
import io.github.fiifoo.scarl.action.validate.ActionValidator
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.ItemId
import io.github.fiifoo.scarl.core.entity.Selectors.getContainerItems
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.item.Equipment.ArmorSlot
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.game.player.PlayerAutomation
import io.github.fiifoo.scarl.game.{CalculateBrains, RunGame, RunState}

import scala.concurrent.ExecutionContext

sealed trait InMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage])
}

case class AutoMove(direction: Option[Int] = None, destination: Option[Location] = None) extends InMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    destination.orElse(PlayerAutomation.getDestination(state, direction)) flatMap (destination => {
      PlayerAutomation.getMoveAction(state, destination) map (action => {
        val (result, _) = GameAction(action)(state)

        if (!result.ended && PlayerAutomation.shouldContinueMoving(state, result, destination)) {
          (result, Some(this.copy(destination = Some(destination))))
        } else {
          (result, None)
        }
      })
    }) getOrElse {
      (state, None)
    }
  }
}

case object DebugFovQuery extends InMessage with DebugMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    val message = DebugFov(state.fov.locations)

    (state.copy(outMessages = message :: state.outMessages), None)
  }
}

case object DebugWaypointQuery extends InMessage with DebugMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    val message = DebugWaypoint(state.instance.cache.waypointNetwork)

    (state.copy(outMessages = message :: state.outMessages), None)
  }
}

case class GameAction(action: Action) extends InMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    val nextState = if (ActionValidator(state.instance, state.game.player, this.action)) {
      this.run(state)
    } else {
      state
    }

    (nextState, None)
  }

  private def run(initial: RunState)(implicit ec: ExecutionContext): RunState = {
    var state = initial

    state = RunGame(state, Some(this.action))
    val brains = CalculateBrains(state)
    state = RunGame(state)

    state.copy(brains = Some(brains))
  }
}

case object InventoryQuery extends InMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    val message = PlayerInventory(
      inventory = getContainerItems(state.instance)(state.game.player) map (_ (state.instance)),
      equipments = state.instance.equipments.getOrElse(state.game.player, Map())
    )

    (state.copy(outMessages = message :: state.outMessages), None)
  }
}

case class SetEquipmentSet(set: Int) extends InMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    val nextState = if (this.set != state.game.settings.equipmentSet) {
      this.execute(state)
    } else {
      state
    }

    (nextState, None)
  }

  private def execute(initial: RunState)(implicit ec: ExecutionContext): RunState = {
    var state = initial

    val settings = state.game.settings.changeEquipmentSet(this.set, this.getCurrentWeapons(state))
    val message = PlayerSettings(settings)
    val action = EquipWeaponsAction(this.getNewWeapons(state))

    state = state.copy(
      game = state.game.copy(settings = settings),
      outMessages = message :: state.outMessages
    )
    val (_state, _) = GameAction(action)(state)
    state = _state
    if (!state.ended) {
      val (_state, _) = InventoryQuery(state)
      state = _state
    }

    state
  }

  private def getCurrentWeapons(state: RunState): Map[Equipment.Slot, ItemId] = {
    val equipments = state.instance.equipments.getOrElse(state.game.player, Map())

    equipments filterKeys (!_.isInstanceOf[ArmorSlot])
  }

  private def getNewWeapons(state: RunState): Map[Equipment.Slot, ItemId] = {
    val weapons = state.game.settings.equipmentSets.getOrElse(this.set, Map())
    val s = state.instance

    weapons filter (x => {
      val (_, item) = x

      s.entities.isDefinedAt(item) && item(s).container == state.game.player
    })
  }
}

case class SetQuickItem(slot: Int, item: Option[ItemKindId]) extends InMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    val settings = state.game.settings.setQuickItem(this.slot, this.item)
    val message = PlayerSettings(settings)

    val nextState = state.copy(
      game = state.game.copy(settings = settings),
      outMessages = message :: state.outMessages
    )

    (nextState, None)
  }
}
