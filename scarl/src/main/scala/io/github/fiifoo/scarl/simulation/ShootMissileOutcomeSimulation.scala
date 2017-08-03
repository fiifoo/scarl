package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.action.ShootMissileAction
import io.github.fiifoo.scarl.ai.tactic.RoamTactic
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.{ActorId, CreatureId}
import io.github.fiifoo.scarl.core.{ActorQueue, Location, State}
import io.github.fiifoo.scarl.geometry.WaypointNetwork

import scala.collection.SortedSet

object ShootMissileOutcomeSimulation {

  val turnLimit = 5

  def apply(instance: State, shooter: CreatureId, location: Location): Outcome = {
    val enemies = shooter(instance).faction(instance).enemies
    val simulation = createSimulation(enemies)
    val action = ShootMissileAction(location)

    ActionOutcomeSimulation(queueActors(instance, shooter, location), simulation, action)
  }

  private def createSimulation(enemies: Set[FactionId]): Simulation[Outcome] = {
    new Simulation[Outcome](
      listener = new ExplosionOutcomeListener(enemies),
      ai = RoamTactic,
      turnLimit = turnLimit
    )
  }

  private def queueActors(s: State, shooter: CreatureId, location: Location): State = {
    implicit val ordering = ActorQueue.ordering

    val actors = WaypointNetwork.nearbyCreatures(s, Set(shooter(s).location, location)) + shooter
    val queue = (actors map (actor => (actor: ActorId, actor(s).tick))).to[SortedSet]

    s.copy(cache = s.cache.copy(
      actorQueue = ActorQueue(queue)
    ))
  }
}
