package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.action.Utils._
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.effect.TickEffect
import io.github.fiifoo.scarl.effect.combat.ShootMissileEffect

case class ShootMissileAction(location: Location, missile: CreatureKindId) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    val tick = TickEffect(actor)

    val creature = actor(s)
    val consumption = getCreatureStats(s)(actor).launcher.consumption

    shortage(creature, consumption) map (List(tick, _)) getOrElse {
      List(
        Some(tick),
        Some(ShootMissileEffect(actor, location, actor(s).location, missile)),
        consume(creature, consumption)
      ).flatten
    }
  }
}
