package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.mutation.{ConduitExitMutation, RemovableEntityMutation, RemoveEntitiesMutation, ResetGoalsMutation}
import io.github.fiifoo.scarl.core.world.{ConduitId, Traveler}
import io.github.fiifoo.scarl.game.player.{PlayerFov, PlayerInfo}
import io.github.fiifoo.scarl.world.{GenerateArea, SiteId, WorldState}

object ChangeArea {

  def apply(destination: SiteId, conduit: Option[ConduitId] = None)(state: RunState): RunState = {
    val traveler = Traveler(state.instance, state.game.player)

    val change =
      closeInstance _ andThen
        openInstance(traveler, destination, conduit) andThen
        updateGame(destination) andThen
        updateRun(destination)

    change(state)
  }

  private def closeInstance(state: RunState): RunState = {
    val world = state.game.world
    val instance = state.instance

    val mutate =
      ResetGoalsMutation().apply _ andThen
        RemovableEntityMutation(state.game.player).apply andThen
        RemoveEntitiesMutation().apply

    state.copy(game = state.game.copy(
      world = world.copy(
        goals = world.goals ++ instance.creature.goals,
        states = world.states + (state.game.area -> mutate(instance))
      )
    ))
  }

  private def openInstance(traveler: Traveler, destination: SiteId, conduit: Option[ConduitId])
                          (state: RunState): RunState = {
    val open = (world: WorldState) => {
      world.states.get(destination) map (instance => {
        world.copy(
          states = world.states + (destination -> instance.copy(idSeq = state.instance.idSeq))
        )
      }) getOrElse {
        GenerateArea(destination, state.instance.rng, state.instance.idSeq)(world)
      }
    }

    val applyExit = (world: WorldState) => {
      val instance = world.states(destination)
      val location = conduit map (instance.conduits.exits(_)) getOrElse {
        val (random, _) = instance.rng()

        Rng.nextChoice(random, instance.gateways)
      }

      val mutate = ConduitExitMutation(traveler, location)

      world.copy(
        states = world.states + (destination -> mutate(instance))
      )
    }

    val calculate =
      open andThen
        applyExit

    state.copy(game = state.game.copy(
      world = calculate(state.game.world)
    ))
  }

  private def updateGame(destination: SiteId)(state: RunState): RunState = {
    state.copy(
      game = state.game.copy(
        area = destination,
        maps = state.game.maps + (state.game.area -> state.areaMap)
      )
    )
  }

  private def updateRun(destination: SiteId)(state: RunState): RunState = {
    val instance = state.game.world.states(destination)

    state.copy(
      areaMap = state.game.maps.getOrElse(destination, Map()),
      fov = PlayerFov(),
      instance = instance,
      playerInfo = PlayerInfo(instance, state.game.player)
    )
  }
}
