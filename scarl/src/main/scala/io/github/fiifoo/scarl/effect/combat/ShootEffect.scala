package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.entity.{CreatureId, EntityId}
import io.github.fiifoo.scarl.core.geometry.{Line, Location, Obstacle}

case class ShootEffect(attacker: CreatureId,
                       location: Location,
                       parent: Option[Effect] = None
                      ) extends Effect {

  def apply(s: State): EffectResult = {

    def getTarget(location: Location): Option[EntityId] = {
      Obstacle.shot(s)(location) collect {
        case target: EntityId if target != attacker => target
      }
    }

    val range = getCreatureStats(s)(attacker).ranged.range
    val from = attacker(s).location
    val path = Line(from, location) take range + 1
    val to = path find (getTarget(_).isDefined) getOrElse path.last
    val target = getTarget(to)

    EffectResult(ShotEffect(attacker, from, to, target, Some(this)))
  }
}
