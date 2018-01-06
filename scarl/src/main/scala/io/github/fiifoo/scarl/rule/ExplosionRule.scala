package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.entity.{EntityId, ItemId, WallId}
import io.github.fiifoo.scarl.core.geometry.{Line, Location, Obstacle, Shape}

object ExplosionRule {

  def apply(s: State, location: Location, stats: Explosive): Set[Location] = {
    val step = stats.blast / stats.radius / 2
    val obstacle = Obstacle.explosion(s) _

    Shape.circle(location, stats.radius) filterNot blocked(s, step, location, stats, obstacle)
  }

  private def blocked(s: State,
                      step: Int,
                      source: Location,
                      stats: Explosive,
                      obstacle: Location => Option[EntityId]
                     )(target: Location): Boolean = {
    val (result, _) = (Line(source, target).tail foldLeft(false, stats.blast)) ((carry, location) => {
      val (result, blast) = carry

      if (result) {
        carry
      } else {
        (obstacle(location) collect {
          case item: ItemId => calculateBlast(item(s).door flatMap (_.hardness), blast, step)
          case wall: WallId => calculateBlast(wall(s).hardness, blast, step)
        }) getOrElse(false, Math.max(blast - step, 0))
      }
    })

    result
  }

  private def calculateBlast(hardness: Option[Int], blast: Int, step: Int): (Boolean, Int) = {
    hardness map (hardness => {
      if (hardness > blast) {
        (true, 0)
      } else {
        (false, Math.max(blast - hardness - step, 0))
      }
    }) getOrElse(true, 0)
  }
}
