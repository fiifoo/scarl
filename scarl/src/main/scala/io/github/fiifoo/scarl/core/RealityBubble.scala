package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResolver}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.{RngMutation, StoredActorsMutation, TacticMutation, TickMutation}

object RealityBubble {

  def apply(initial: State,
            ai: (CreatureId) => Tactic,
            listener: Listener = new Listener()
           ): (RealityBubble, State) = {

    val actors = new ActorQueue()
    val mutated = actors.enqueueNewActors(initial)

    (new RealityBubble(actors, ai, listener), mutated)
  }

}

class RealityBubble(actors: ActorQueue,
                    ai: (CreatureId) => Tactic,
                    listener: Listener = new Listener()
                   ) {

  private val resolveEffects = new EffectResolver(listener.effect)

  def nextActor: Option[ActorId] = actors.headOption

  def apply(state: State, action: Option[Action] = None): State = {
    var s = state

    dequeue(s).foreach(actor => {
      s = TickMutation(actor(s).tick)(s)

      val effects = actor match {
        case creature: CreatureId =>
          val (effects, mutated) = handleCreature(s, creature, action)
          s = mutated

          effects
        case status: ActiveStatusId => status(s)(s)
        case _ => throw new Exception("Unknown actor type")
      }

      s = resolveEffects(s, effects)
      s = actors.enqueueNewActors(s)
      enqueue(s, actor)
    })

    s
  }

  def save(s: State): State = {
    val storeActors = actors.dequeueAll filter s.entities.isDefinedAt

    StoredActorsMutation(storeActors)(s)
  }

  private def handleCreature(s: State,
                             actor: CreatureId,
                             action: Option[Action]
                            ): (List[Effect], State) = {

    action map (_ (s, actor) -> s) getOrElse {
      val (random, nextRng) = s.rng()
      val (tactic, action) = s.tactics.get(actor) map (_ (s, random)) getOrElse ai(actor)(s, random)

      val nextState = TacticMutation(tactic)(RngMutation(nextRng)(s))

      (action(nextState, actor), nextState)
    }
  }

  private def dequeue(s: State): Option[ActorId] = {
    if (actors.nonEmpty) {
      val actor = actors.dequeue()
      if (s.entities.isDefinedAt(actor)) {
        return Some(actor)
      }
    }

    None
  }

  private def enqueue(s: State, actor: ActorId): Unit = {
    if (s.entities.isDefinedAt(actor)) {
      actors.enqueue(actor, actor(s).tick)
    }
  }
}
