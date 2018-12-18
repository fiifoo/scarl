package game

import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.game.GameState
import io.github.fiifoo.scarl.world.{CreateWorld, WorldAssets}

import scala.util.Random

object GenerateGame {

  def apply(assets: WorldAssets): GameState = {
    val rng = Rng(Random.nextInt())
    val world = assets.worlds.values.head
    val character = world.characters.head

    val (worldState, player) = CreateWorld(assets, world, character, rng)

    GameState(world.start, player, worldState)
  }
}
