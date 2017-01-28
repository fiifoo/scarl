package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResolver}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.{SeedMutation, TacticMutation, TickMutation}

import scala.util.Random

class RealityBubble(var s: State, ai: (CreatureId) => Tactic) {

  val random = new Random(s.seed)
  val actors = new ActorQueue()
  s = actors.enqueueNewActors(s)

  def nextActor: Option[ActorId] = if (actors.nonEmpty) Some(actors.head) else None

  def be(action: Option[Action] = None): Unit = {
    dequeue().foreach(actor => {
      s = TickMutation(actor(s).tick)(s)

      val effects = actor(s) match {
        case creature: Creature => handleCreature(creature, action)
        case status: ActiveStatus => status.activate(s)
        case _ => throw new Exception("Unknown actor type")
      }

      s = EffectResolver(s, effects)
      s = SeedMutation(random.nextInt())(s)
      s = actors.enqueueNewActors(s)
      enqueue(actor)
    })
  }

  private def handleCreature(actor: Creature, action: Option[Action]): List[Effect] = {
    action map (action => action(s, actor)) getOrElse {
      val (tactic, action) = s.tactics.get(actor.id) map (tactic => tactic(s)) getOrElse ai(actor.id)(s)
      s = TacticMutation(tactic)(s)

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
