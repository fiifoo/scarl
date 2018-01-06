package game

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.game.GameState
import io.github.fiifoo.scarl.world.{CreateWorld, WorldAssets}

import scala.util.Random

object GenerateGame {

  def apply(assets: WorldAssets): GameState = {
    val rng = Rng(Random.nextInt())
    val area = AreaId("start-level")
    val (world, player) = CreateWorld(assets, area, CreatureKindId("player"), rng)

    GameState(area, player, world)
  }
}
