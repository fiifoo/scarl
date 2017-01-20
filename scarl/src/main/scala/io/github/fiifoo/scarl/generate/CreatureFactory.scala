package io.github.fiifoo.scarl.generate

import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}

import scala.util.Random

class CreatureFactory(locationConstraint: (Int, Int) = (80, 25)) {

  def create(id: CreatureId,
             location: Location = Location(0, 0),
             tick: Int = 1,
             health: Int = 1,
             damage: Int = 0
            ): Creature = Creature(id, location, tick, health, damage)

  def generate(s: State, count: Int, prototype: Creature = create(CreatureId(0))): State = {

    val random =  new Random(s.seed)

    (0 until count).foldLeft(s)((s, i) => {
      val id = CreatureId(s.nextEntityId)
      val location = generateLocation(random)
      val tick = s.tick
      val creature = prototype.copy(id, location, tick)

      NewEntityMutation(creature)(s)
    })
  }

  private def generateLocation(random: Random): Location = {
    val x = random.nextInt(locationConstraint._1)
    val y = random.nextInt(locationConstraint._2)

    Location(x, y)
  }
}
