package io.github.fiifoo.scarl.generate

import io.github.fiifoo.scarl.core.entity.Creature.Stats
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, FactionId}
import io.github.fiifoo.scarl.core.mutation.{NewEntityMutation, RngMutation}
import io.github.fiifoo.scarl.core.{Location, State}

import scala.util.Random

object CreatureFactory {
  def apply(locationConstraint: (Int, Int) = (80, 25)): CreatureFactory = new CreatureFactory(locationConstraint)
}

class CreatureFactory(locationConstraint: (Int, Int)) {

  def create(faction: FactionId,
             id: CreatureId = CreatureId(0),
             location: Location = Location(0, 0),
             tick: Int = 1,
             damage: Int = 0,
             stats: Stats = Stats(health = 10, attack = 10, defence = 10, damage = 10, armor = 5)
            ): Creature = Creature(id, faction, location, tick, damage, stats)

  def generate(s: State, count: Int, faction: FactionId): State = {
    val (random, rng) = s.rng()
    val _s = RngMutation(rng)(s)

    val prototype = create(faction)

    (0 until count).foldLeft(_s)((s, _) => {
      val id = CreatureId(s.nextEntityId)
      val location = generateLocation(random)
      val tick = s.tick
      val creature = prototype.copy(id, location = location, tick = tick)

      NewEntityMutation(creature)(s)
    })
  }

  private def generateLocation(random: Random): Location = {
    val x = random.nextInt(locationConstraint._1)
    val y = random.nextInt(locationConstraint._2)

    Location(x, y)
  }
}
