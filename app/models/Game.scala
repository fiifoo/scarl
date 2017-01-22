package models

import io.github.fiifoo.scarl.ai.ActionDecider
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{RealityBubble, State}
import io.github.fiifoo.scarl.generate.CreatureFactory
import io.github.fiifoo.scarl.geometry.Fov

class Game() {

  class ActionReceiver(player: Player) {
    def apply(action: Action): Unit = {
      runPlayer(player, action)
    }
  }

  private val playerId = CreatureId(1)

  private val bubble = new RealityBubble(
    new CreatureFactory().generate(State(), 100),
    ActionDecider
  )

  def receivePlayer(player: Player): ActionReceiver = {
    runNpc()
    player.receive(entities, fov)

    new ActionReceiver(player)
  }

  private def runPlayer(player: Player, action: Action): Unit = {
    if (isPlayerTurn) {
      bubble.be(Some(action))
      player.receive(entities, fov)

      runNpc()
      player.receive(entities, fov)
    }
  }

  private def runNpc(): Unit = {
    while (isNpcTurn) {
      bubble.be()
    }
  }

  private def isPlayerTurn = bubble.nextActor.contains(playerId)

  private def isNpcTurn = bubble.actors.nonEmpty && !bubble.nextActor.contains(playerId)

  private def entities = bubble.s.entities

  private def fov = {
    val s = bubble.s
    val location = playerId(s).location
    Fov(s)(location, 10)
  }
}
