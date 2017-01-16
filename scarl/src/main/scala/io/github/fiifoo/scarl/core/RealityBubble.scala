package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.action.{Action, ActionDecider}
import io.github.fiifoo.scarl.core.effect.EffectResolver
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.TickMutation

class RealityBubble(var s: State, actionDecider: ActionDecider) {

  val actorQueue = new ActorQueue()
  s = actorQueue.enqueueNewActors(s)

  def be(action: Option[Action] = None): Boolean = {
    val actor = dequeue()

    actor.foreach(actor => {
      s = TickMutation(actor(s).tick)(s)

      val effects = actor(s) match {
        case creature: Creature => action.getOrElse(actionDecider(s, creature))(s, creature)
        case status: ActiveStatus => status.activate(s)
        case _ => throw new Exception("Unknown actor type")
      }

      s = EffectResolver(s, effects)
      s = actorQueue.enqueueNewActors(s)
      enqueue(actor)
    })

    actor.isDefined
  }

  private def dequeue(): Option[ActorId] = {
    while (!actorQueue.isEmpty) {
      val actor = actorQueue.dequeue()
      if (s.entities.isDefinedAt(actor)) {
        return Some(actor)
      }
    }

    None
  }

  private def enqueue(actor: ActorId): Unit = {
    if (s.entities.isDefinedAt(actor)) {
      actorQueue.enqueue(actor, actor(s).tick)
    }
  }
}
