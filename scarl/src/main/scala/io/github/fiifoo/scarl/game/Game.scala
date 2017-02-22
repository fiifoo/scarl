package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.ai.tactic.RoamTactic
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.{CombinedEffectListener, Effect, EffectListener}
import io.github.fiifoo.scarl.core.{Listener, RealityBubble, State}
import io.github.fiifoo.scarl.effect.DeathEffect
import io.github.fiifoo.scarl.game.message.MessageBuilder
import io.github.fiifoo.scarl.geometry.Fov

class Game(out: OutConnection, player: Player, initial: State) {

  private var gameOver = false
  private val messages = new MessageBuilder(player)
  private val statistics = new StatisticsBuilder()

  object GameOverListener extends EffectListener {
    def apply(s: State, effect: Effect): Unit = {
      effect match {
        case effect: DeathEffect if effect.target == player.creature => gameOver = true
        case _ =>
      }
    }
  }

  private val bubble = new RealityBubble(
    s = initial,
    ai = RoamTactic,
    listener = new Listener(effect = new CombinedEffectListener(List(
      messages,
      statistics,
      GameOverListener
    )))
  )

  def receive(action: Action): Unit = {
    if (!gameOver) {
      run(action)
    }
  }

  private def initialize(): Unit = {
    updateFov()
    runNpc()
    updateFov()

    sendInitial()
  }

  private def run(action: Action): Unit = {
    bubble.be(Some(action))
    updateFov()
    runNpc()
    updateFov()

    if (gameOver) {
      sendFinal()
    } else {
      send()
    }
  }

  private def runNpc(): Unit = {
    while (!gameOver && npcTurn) {
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

  private def sendInitial(): Unit = {
    out(s, messages.extract(), kinds = Some(s.kinds))
  }

  private def send(): Unit = {
    out(s, messages.extract())
  }

  private def sendFinal(): Unit = {
    out(s, messages.extract(), statistics = Some(statistics.get()))
  }

  private def s = bubble.s

  private def npcTurn = bubble.actors.nonEmpty && !bubble.nextActor.contains(player.creature)

  initialize()
}
