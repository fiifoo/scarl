package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResolver}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.{SeedMutation, TacticMutation, TickMutation}

import scala.util.Random

class RealityBubble(var s: State,
                    ai: (CreatureId) => Tactic,
                    logger: Logger = new Logger()
                   ) {

  val random = new Random(s.seed)
  val resolveEffect = new EffectResolver(logger.effect)
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
      s = SeedMutation(random.nextInt())(s)
      s = actors.enqueueNewActors(s)
      enqueue(actor)
    })
  }

  private def handleCreature(actor: CreatureId, action: Option[Action]): List[Effect] = {
    action map (action => action(s, actor)) getOrElse {
      val (tactic, action) = s.tactics.get(actor) map (_ (s)) getOrElse ai(actor)(s)
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
