package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.action.ShootMissileAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.Selectors.getLocationWaypoint
import io.github.fiifoo.scarl.core.entity.{ActorId, ActorQueue, CreatureId, Selectors}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.CreatureKindId

import scala.collection.SortedSet

object ShootMissileOutcomeSimulation {

  val turnLimit = 5

  def apply(instance: State, shooter: CreatureId, location: Location, missile: CreatureKindId): Outcome = {
    val enemies = shooter(instance).faction(instance).enemies
    val simulation = createSimulation(enemies)
    val action = ShootMissileAction(location, missile)

    ActionOutcomeSimulation(queueActors(instance, shooter, location), simulation, action)
  }

  private def createSimulation(enemies: Set[FactionId]): Simulation[Outcome] = {
    new Simulation[Outcome](
      listener = new ExplosionOutcomeListener(enemies),
      turnLimit = turnLimit
    )
  }

  private def queueActors(s: State, shooter: CreatureId, location: Location): State = {
    implicit val ordering = ActorQueue.ordering

    val waypoints = Set(shooter(s).location, location) flatMap getLocationWaypoint(s)
    val actors = (waypoints flatMap Selectors.getWaypointCreatures(s)) + shooter
    val queue = (actors map (actor => (actor: ActorId, actor(s).tick))).to(SortedSet)

    s.copy(cache = s.cache.copy(
      actorQueue = ActorQueue(queue)
    ))
  }
}
