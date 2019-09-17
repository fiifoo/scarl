package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.action.validate.ActionValidator
import io.github.fiifoo.scarl.action.{EquipWeaponsAction, UseItemAction}
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.ItemId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.item.Equipment.ArmorSlot
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.game.player.PlayerAutomation
import io.github.fiifoo.scarl.game.{CalculateBrains, RunGame, RunState, WorldAction => WorldActionInstance}
import io.github.fiifoo.scarl.power.ScanPower

import scala.concurrent.ExecutionContext

sealed trait InMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage])
}

case class AutoMove(direction: Option[Int] = None,
                    destination: Option[Location] = None,
                    explore: Option[Location] = None
                   ) extends InMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    PlayerAutomation.apply(state, this.direction, this.destination, this.explore) map (x => {
      val (destination, explore, action) = x
      val (result, _) = GameAction(action)(state)

      if (result.ended || PlayerAutomation.stop(state, result)) {
        (result, None)
      } else {
        (result, Some(this.copy(destination = Some(destination), explore = Some(explore))))
      }
    }) getOrElse {
      (state, None)
    }
  }
}

case object DebugFovQuery extends InMessage with DebugMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    val message = DebugFov(state)

    (state.addMessage(message), None)
  }
}

case object DebugWaypointQuery extends InMessage with DebugMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    val message = DebugWaypoint(state)

    (state.addMessage(message), None)
  }
}

case class GameAction(action: Action) extends InMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    if (ActionValidator(state.instance, state.game.player, this.action)) {
      val nextState = this.run(state)

      val nextMessage: Option[InMessage] = action match {
        case action: UseItemAction => action.target(state.instance).usable collect {
          case _: ScanPower => SignalMapQuery
        }
        case _ => None
      }

      (nextState, nextMessage)
    } else {
      (state, None)
    }
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
    val message = PlayerInventory(state)

    (state.addMessage(message), None)
  }
}

case object SignalMapQuery extends InMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    val message = SignalMap(state)

    (state.addMessage(message), None)
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

    state = state.addMessage(message).copy(
      game = state.game.copy(settings = settings),
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

    equipments filter (!_._1.isInstanceOf[ArmorSlot])
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

    val nextState = state.addMessage(message).copy(
      game = state.game.copy(settings = settings),
    )

    (nextState, None)
  }
}

case class WorldAction(action: WorldActionInstance) extends InMessage {
  def apply(state: RunState)(implicit ec: ExecutionContext): (RunState, Option[InMessage]) = {
    val nextState = action.apply(state) map (state => {
      state.addMessage(WorldInfo(state))
    }) getOrElse state

    (nextState, None)
  }
}
