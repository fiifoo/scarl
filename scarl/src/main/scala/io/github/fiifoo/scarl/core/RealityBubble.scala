package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResolver}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation._

object RealityBubble {

  case class Result(state: State,
                    actor: ActorId,
                    action: Option[Action],
                    effects: List[Effect])

  def apply(state: State, fixedAction: Option[Action] = None): Option[Result] = {
    dequeue(state) map (x => {
      val (actor, s) = x

      (actor, InitializeTickMutation(actor)(s))
    }) map (x => {
      val (actor, s) = x

      actor match {
        case creature: CreatureId =>
          val (ns, action, effects) = handleCreature(s, creature, fixedAction)

          resolve(ns, actor, Some(action), effects)
        case status: ActiveStatusId =>
          val effects = status(s)(s)

          resolve(s, actor, None, effects)
        case global: GlobalActorId =>
          val effects = GlobalEffects(s, global)

          resolve(s, actor, None, effects)
        case _ => throw new Exception("Unknown actor type")
      }
    })
  }

  private def resolve(state: State,
                      actor: ActorId,
                      action: Option[Action],
                      effects: List[Effect]
                     ): Result = {
    var s = TickMutation(actor(state).tick)(state)

    val (ns, resolved) = EffectResolver(s, effects)
    s = enqueue(ns, actor)

    Result(s, actor, action, resolved)
  }

  private def handleCreature(s: State,
                             actor: CreatureId,
                             fixedAction: Option[Action]
                            ): (State, Action, List[Effect]) = {
    fixedAction map (action => {
      val effects = action(s, actor)

      (s, action, effects)
    }) getOrElse {
      val (random, nextRng) = s.rng()
      val (tactic, action) = s.creature.tactics.get(actor) flatMap (_ (s, actor, random)) getOrElse
        actor(s).behavior.behavior(s, actor, random)

      val ns = CreatureTacticMutation(actor, Some(tactic))(RngMutation(nextRng)(s))
      val effects = action(ns, actor)

      (ns, action, effects)
    }
  }

  private def dequeue(s: State): Option[(ActorId, State)] = {
    s.cache.actorQueue.dequeue map (x => {
      val (actor, nextQueue) = x
      val ns = s.copy(cache = s.cache.copy(
        actorQueue = nextQueue
      ))

      (actor, ns)
    })
  }

  private def enqueue(s: State, actor: ActorId): State = {
    val nextQueue = s.cache.actorQueue.enqueue(actor(s))

    s.copy(cache = s.cache.copy(
      actorQueue = nextQueue
    ))
  }
}
