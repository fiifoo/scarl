package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResolver}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.{RngMutation, TacticMutation, TickMutation}

class RealityBubble(var s: State,
                    ai: (CreatureId) => Tactic,
                    listener: Listener = new Listener()
                   ) {
  val resolveEffect = new EffectResolver(listener.effect)
  val actors = new ActorQueue()
  s = actors.enqueueNewActors(s)

  def nextActor: Option[ActorId] = actors.headOption

  def be(action: Option[Action] = None): Unit = {
    dequeue().foreach(actor => {
      s = TickMutation(actor(s).tick)(s)

      val effects = actor match {
        case creature: CreatureId => handleCreature(creature, action)
        case status: ActiveStatusId => status(s)(s)
        case _ => throw new Exception("Unknown actor type")
      }

      s = resolveEffect(s, effects)
      s = actors.enqueueNewActors(s)
      enqueue(actor)
    })
  }

  private def handleCreature(actor: CreatureId, action: Option[Action]): List[Effect] = {
    action map (action => action(s, actor)) getOrElse {
      val (tactic, action, rng) = s.tactics.get(actor) map (_ (s, s.rng)) getOrElse ai(actor)(s, s.rng)
      s = TacticMutation(tactic)(RngMutation(rng)(s))

      action(s, actor)
    }
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
