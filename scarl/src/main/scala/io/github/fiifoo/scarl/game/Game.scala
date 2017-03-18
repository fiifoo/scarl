package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.ai.tactic.RoamTactic
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.CombinedEffectListener
import io.github.fiifoo.scarl.core.{Listener, RealityBubble, State}
import io.github.fiifoo.scarl.geometry.Fov
import io.github.fiifoo.scarl.message.MessageBuilder

import scala.annotation.tailrec

class Game(out: OutConnection,
           player: Player,
           initial: State
          ) {

  private val messages = new MessageBuilder(player)
  private val statistics = new StatisticsBuilder()
  private var (bubble, state) = createBubble(initial)

  def receive(action: Action): Unit = {
    run(Some(action))
  }

  private def initialize(): Unit = {
    sendInitial(state)
    run(None)
  }

  private def run(action: Option[Action]): Unit = {
    val s = process(bubble, state, action)

    if (gameOver(s)) {
      sendFinal(s)
    } else {
      send(s)
    }

    state = s
  }

  @tailrec
  private def process(b: RealityBubble, s: State, action: Option[Action]): State = {
    if (gameOver(s)) {
      return s
    }

    if (b.nextActor.contains(player.creature)) {
      if (action.isDefined) {
        process(b, b(s, action), None)
      } else {
        s
      }
    } else {
      process(b, b(s, None), action)
    }
  }

  private def sendInitial(s: State): Unit = {
    updateFov(s)
    out(s, messages.extract(), kinds = Some(s.kinds))
  }

  private def send(s: State): Unit = {
    updateFov(s)
    out(s, messages.extract())
  }

  private def sendFinal(s: State): Unit = {
    out(s, messages.extract(), statistics = Some(statistics.get()))
  }

  private def updateFov(s: State): Unit = {
    player.fov = Fov(s)(player.creature(s).location, 10)
  }

  private def gameOver(s: State): Boolean = {
    !s.entities.isDefinedAt(player.creature)
  }

  private def createBubble(s: State): (RealityBubble, State) = {
    RealityBubble(
      initial = s,
      ai = RoamTactic,
      listener = new Listener(effect = new CombinedEffectListener(List(
        messages,
        statistics
      )))
    )
  }

  initialize()
}
