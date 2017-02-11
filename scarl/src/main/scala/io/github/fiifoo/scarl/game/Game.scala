package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.ai.tactic.RoamTactic
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.{Logger, RealityBubble}
import io.github.fiifoo.scarl.game.message.MessageBuilder
import io.github.fiifoo.scarl.generate.Generate
import io.github.fiifoo.scarl.geometry.Fov

class Game(out: OutConnection, player: Player) {

  private val messages = new MessageBuilder(player)

  private val bubble = new RealityBubble(
    s = Generate(),
    ai = RoamTactic,
    logger = new Logger(effect = messages.receive)
  )

  def receive(action: Action): Unit = {
    if (!gameOver) {
      run(action)
      send()
    }
  }

  private def initialize(): Unit = {
    updateFov()
    runNpc()
    updateFov()
    send()
  }

  private def run(action: Action): Unit = {
    bubble.be(Some(action))
    updateFov()

    if (!gameOver) {
      runNpc()
      updateFov()
    }
  }

  private def runNpc(): Unit = {
    while (npcTurn) {
      bubble.be()
    }
  }

  private def updateFov(): Unit = {
    if (gameOver) {
      player.fov = Set()
    } else {
      player.fov = Fov(s)(player.creature(s).location, 10)
    }
  }

  private def send(): Unit = {
    out(s, messages.extract())
  }

  private def s = bubble.s

  private def gameOver = !bubble.s.entities.isDefinedAt(player.creature)

  private def npcTurn = bubble.actors.nonEmpty && !bubble.nextActor.contains(player.creature)

  initialize()
}