package models

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Rng
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.world.{WorldManager, WorldState}

import scala.util.Random

object GenerateWorld {

  def apply(): (WorldManager, WorldState, AreaId, CreatureId) = {
    val rng = Rng(Random.nextInt())

    val manager = new WorldManager(Data.areas, Data.factions, Data.kinds, Data.templates)
    val area = AreaId("first")
    val (world, player) = manager.create(area, CreatureKindId("hero"), rng)

    (manager, world, area, player)
  }
}
