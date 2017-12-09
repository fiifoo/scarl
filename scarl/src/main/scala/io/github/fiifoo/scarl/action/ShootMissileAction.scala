package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.action.Utils._
import io.github.fiifoo.scarl.core.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.{Location, Selectors, State, Time}
import io.github.fiifoo.scarl.effect.combat.ShootMissileEffect

case class ShootMissileAction(location: Location) extends Action {
  val cost = (Time.turn * 1.5).toInt

  def apply(s: State, actor: CreatureId): List[Effect] = {
    val tick = TickEffect(actor, cost)

    getMissile(s, actor) map (missile => {
      val creature = actor(s)
      val consumption = getCreatureStats(s)(actor).launcher.consumption

      shortage(creature, consumption) map (List(tick, _)) getOrElse {
        List(
          Some(tick),
          Some(ShootMissileEffect(actor, location, actor(s).location, missile)),
          consume(creature, consumption)
        ).flatten
      }
    }) getOrElse List(tick)
  }

  private def getMissile(s: State, actor: CreatureId): Option[CreatureKindId] = {
    Selectors.getCreatureStats(s)(actor).launcher.missile
  }
}
