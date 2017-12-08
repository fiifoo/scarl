package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.{Location, Selectors, State, Time}
import io.github.fiifoo.scarl.effect.combat.ShootMissileEffect

case class ShootMissileAction(location: Location) extends Action {
  val cost = (Time.turn * 1.5).toInt

  def apply(s: State, actor: CreatureId): List[Effect] = {
    getMissile(s, actor) map (missile => {
      List(
        TickEffect(actor, cost),
        ShootMissileEffect(actor, location, actor(s).location, missile)
      )
    }) getOrElse List()
  }

  private def getMissile(s: State, actor: CreatureId): Option[CreatureKindId] = {
    Selectors.getCreatureStats(s)(actor).launcher.missile
  }
}
