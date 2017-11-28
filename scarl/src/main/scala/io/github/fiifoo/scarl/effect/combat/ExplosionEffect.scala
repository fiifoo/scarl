package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{EntityId, ItemId, LocatableId, WallId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.{Line, Obstacle, Shape}

case class ExplosionEffect(source: LocatableId,
                           location: Location,
                           stats: Explosive,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    val obstacle = Obstacle.explosion(s) _
    val step = stats.blast / stats.radius
    val locations = Shape.circle(location, stats.radius) filterNot blocked(s, step, obstacle)

    EffectResult(
      (locations map (ExplosionLocationEffect(source, _, stats, Some(this)))).toList
    )
  }

  private def blocked(s: State, step: Int, obstacle: Location => Option[EntityId])(target: Location): Boolean = {
    val (result, _) = (Line(location, target) foldLeft(false, stats.blast)) ((carry, location) => {
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
