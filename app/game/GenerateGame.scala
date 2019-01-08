package game

import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.game.{GameState, GenerateGame => Generate}
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

object GenerateGame {

  def apply(assets: WorldAssets): GameState = {
    val rng = Rng(Random.nextInt())
    val world = assets.worlds.values.head
    val character = world.characters.head

    Generate(assets, world, character, rng)
  }
}
