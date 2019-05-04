package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.entity.{CreatureId, Signal}
import io.github.fiifoo.scarl.core.geometry.{Distance, Location}
import io.github.fiifoo.scarl.core.{State, Time}

import scala.util.Random

object SignalRule {

  val SignalDuration = Time.turn * 3

  def calculateMap(s: State)(creature: CreatureId): List[Signal] = {
    val from = creature(s).location
    val faction = creature(s).faction
    val sensors = getCreatureStats(s)(creature).sight.sensors

    val signals = s.signals filter (signal => {
      signal.owner.forall(_ == faction)
    })

    signals flatMap calculateSignal(from, sensors)
  }

  def calculateSignal(from: Location, sensors: Int)(signal: Signal): Option[Signal] = {
    val distance = Distance(from, signal.location)
    val strength = calculateStrength(signal.strength, sensors)(distance)
    val radius = calculateRadius(signal.radius)(strength)

    val random = new Random(signal.seed)

    if (rollDetection(random)(strength)) {
      Some(signal.copy(
        location = getRandomLocation(signal.location, radius, random),
        strength = strength,
        radius = radius
      ))
    } else {
      None
    }
  }

  def rollDetection(random: Random)(strength: Int): Boolean = {
    if (strength <= 0) {
      return false
    }

    val max = 4
    val divider = Signal.Medium / max
    val chance = 95 + math.min(strength / divider, max)

    random.nextInt(100) + 1 < chance
  }

  def calculateStrength(initial: Int, sensors: Int)(distance: Int): Int = {
    val divider = 1 + sensors.toDouble / 100

    initial - math.pow(distance / divider, 1.3).toInt
  }

  private def calculateRadius(initial: Int)(strength: Int): Int = {
    val max = 4
    val divider = Signal.Medium / max

    initial + math.max(max - (strength / divider), 0)
  }

  private def getRandomLocation(center: Location, radius: Int, random: Random): Location = {
    val rx = random.nextGaussian() / 2
    val ry = random.nextGaussian() / 2

    val dx = radius * rx
    val dy = radius * ry

    Location(
      center.x + dx.toInt,
      center.y + dy.toInt
    )
  }
}
