package models

import io.github.fiifoo.scarl.ai.tactic.RoamTactic
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.SeedMutation
import io.github.fiifoo.scarl.core.{RealityBubble, State}
import io.github.fiifoo.scarl.generate.{CreatureFactory, WallFactory}

class Game() {

  class ActionReceiver(player: Player) {
    def apply(action: Action): Unit = {
      runPlayer(player, action)
    }
  }

  private val playerId = CreatureId(1)

  private val bubble = new RealityBubble(
    generate(),
    RoamTactic
  )

  def receivePlayer(player: Player): ActionReceiver = {
    runNpc()
    player.receive(s)

    new ActionReceiver(player)
  }

  private def runPlayer(player: Player, action: Action): Unit = {
    if (isPlayerTurn && isPlayerAlive) {
      bubble.be(Some(action))

      if (isPlayerAlive) {
        runNpc()
      }
      player.receive(s)
    }
  }

  private def runNpc(): Unit = {
    while (isNpcTurn) {
      bubble.be()
    }
  }

  private def s = bubble.s

  private def isPlayerAlive = bubble.s.entities.isDefinedAt(playerId)

  private def isPlayerTurn = bubble.nextActor.contains(playerId)

  private def isNpcTurn = bubble.actors.nonEmpty && !bubble.nextActor.contains(playerId)

  private def generate(): State = {
    val creatures = CreatureFactory().generate(State(), 100)
    val seeded = SeedMutation(creatures.seed + 1)(creatures)
    val walls = WallFactory().generate(seeded, 500)

    walls
  }
}
