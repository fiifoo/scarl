package models

import io.github.fiifoo.scarl.action.MoveAction
import io.github.fiifoo.scarl.core.action.{Action, ActionDecider}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.{Location, RealityBubble, State}
import io.github.fiifoo.scarl.generate.CreatureFactory

class Game() {

  object SillyMoveActionDecider extends ActionDecider {
    def apply(s: State, actor: Creature): Action = {
      val from = actor.location
      val to = Location(from.x + 1, from.y)

      MoveAction(to)
    }
  }

  val playerId = CreatureId(1)

  val bubble = new RealityBubble(
    new CreatureFactory().generate(State(), 1000),
    SillyMoveActionDecider
  )

  def receivePlayer(player: Player): ActionReceiver = {
    runNpc()
    player.receive(entities)

    new ActionReceiver(player)
  }

  private def runPlayer(player: Player, action: Action): Unit = {
    if (isPlayerTurn) {
      bubble.be(Some(action))
      player.receive(entities)

      runNpc()
      player.receive(entities)
    }
  }

  private def runNpc(): Unit = {
    while (isNpcTurn) {
      bubble.be()
    }
  }

  private def isPlayerTurn = !bubble.actorQueue.isEmpty && bubble.actorQueue.head == playerId

  private def isNpcTurn = !bubble.actorQueue.isEmpty && bubble.actorQueue.head != playerId

  private def entities = bubble.s.entities

  class ActionReceiver(player: Player) {
    def apply(action: Action): Unit = {
      runPlayer(player, action)
    }
  }

}
