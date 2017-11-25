package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.LocatableId
import io.github.fiifoo.scarl.core.mutation.RemovableEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.{Line, Obstacle, Shape}

case class ExplodeEffect(explosive: LocatableId,
                         location: Location,
                         stats: Explosive,
                         parent: Option[Effect] = None
                        ) extends Effect {

  def apply(s: State): EffectResult = {
    val obstacle = Obstacle.explosion(s) _
    val locations = Shape.circle(location, stats.radius) filterNot
      (Line(location, _).exists(obstacle(_).isDefined))

    EffectResult(
      RemovableEntityMutation(explosive),
      (locations map (ExplosionEffect(explosive, _, stats, Some(this)))).toList
    )
  }
}
