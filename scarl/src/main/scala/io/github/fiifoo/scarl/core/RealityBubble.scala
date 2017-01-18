package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.action.{Action, ActionDecider}
import io.github.fiifoo.scarl.core.effect.EffectResolver
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.TickMutation

class RealityBubble(var s: State, ai: ActionDecider) {

  val actors = new ActorQueue()
  s = actors.enqueueNewActors(s)

  def nextActor: Option[ActorId] = if (actors.nonEmpty) Some(actors.head) else None

  def be(action: Option[Action] = None): Unit = {
    dequeue().foreach(actor => {
      s = TickMutation(actor(s).tick)(s)

      val effects = actor(s) match {
        case creature: Creature => action.getOrElse(ai(s, creature))(s, creature)
        case status: ActiveStatus => status.activate(s)
        case _ => throw new Exception("Unknown actor type")
      }

      s = EffectResolver(s, effects)
      s = actors.enqueueNewActors(s)
      enqueue(actor)
    })
  }

  private def dequeue(): Option[ActorId] = {
    if (actors.nonEmpty) {
      val actor = actors.dequeue()
      if (s.entities.isDefinedAt(actor)) {
        return Some(actor)
      }
    }

    None
  }

  private def enqueue(actor: ActorId): Unit = {
    if (s.entities.isDefinedAt(actor)) {
      actors.enqueue(actor, actor(s).tick)
    }
  }
}
