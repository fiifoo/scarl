package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.entity.{CreatureId, Signal}
import io.github.fiifoo.scarl.core.geometry.{Distance, Location}

import scala.util.Random

object SignalRule {

  def signalMap(s: State)(creature: CreatureId): List[Signal] = {
    val from = creature(s).location
    val faction = creature(s).faction
    val sensors = getCreatureStats(s)(creature).sight.sensors

    val signals = s.signals filter (signal => {
      signal.owner.forall(_ == faction)
    })

    signals flatMap (signal => {
      val distance = Distance(from, signal.location)
      val strength = strengthByDistance(signal.strength, distance, sensors)
      val radius = radiusByStrength(signal.radius, strength)

      if (strength <= 0) {
        None
      } else {
        Some(signal.copy(
          location = randomLocation(signal.location, radius, signal.seed),
          strength = strength,
          radius = radius
        ))
      }
    })
  }

  private def strengthByDistance(strength: Int, distance: Int, sensors: Int): Int = {
    val divider = 1 + sensors.toDouble / 100

    strength - math.pow(distance / divider, 1.3).toInt
  }

  private def radiusByStrength(radius: Int, strength: Int): Int = {
    val max = 5
    val divider = Signal.Strong / max

    radius + math.max(max - (strength / divider), 0)
  }

  private def randomLocation(center: Location, radius: Int, seed: Int): Location = {
    val random = new Random(seed)

    val rx = random.nextGaussian() / 2
    val ry = random.nextGaussian() / 2

    val dx = radius * rx
    val dy = radius * ry

    Location(
      center.x + dx.toInt,
      center.y + dy.toInt,
    )
  }
}
