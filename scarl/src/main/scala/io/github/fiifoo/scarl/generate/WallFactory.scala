package io.github.fiifoo.scarl.generate

import io.github.fiifoo.scarl.core.entity.{KindId, Wall, WallId}
import io.github.fiifoo.scarl.core.mutation.{NewEntityMutation, RngMutation}
import io.github.fiifoo.scarl.core.{Location, State}

import scala.util.Random

object WallFactory {
  def apply(locationConstraint: (Int, Int) = (80, 25)): WallFactory = new WallFactory(locationConstraint)
}

class WallFactory(locationConstraint: (Int, Int)) {

  def create(id: WallId = WallId(0),
             kind: KindId = KindId("wall"),
             location: Location = Location(0, 0)
            ): Wall = Wall(id, kind, location)

  def generate(s: State, count: Int, prototype: Wall = create()): State = {
    val (random, rng) = s.rng()
    val _s = RngMutation(rng)(s)

    (0 until count).foldLeft(_s)((s, _) => {
      val id = WallId(s.nextEntityId)
      val location = generateLocation(random)
      val wall = prototype.copy(id, location = location)

      NewEntityMutation(wall)(s)
    })
  }

  private def generateLocation(random: Random): Location = {
    val x = random.nextInt(locationConstraint._1)
    val y = random.nextInt(locationConstraint._2)

    Location(x, y)
  }
}
