package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.{Location, Selectors, State}
import io.github.fiifoo.scarl.effect.combat.ShootMissileEffect

case class ShootMissileAction(location: Location) extends Action {
  val cost = 150

  def apply(s: State, actor: CreatureId): List[Effect] = {
    getMissileAmmo(s, actor) map (ammo => {
      List(
        TickEffect(actor, cost),
        ShootMissileEffect(actor, location, actor(s).location, ammo)
      )
    }) getOrElse List()
  }

  private def getMissileAmmo(s: State, actor: CreatureId): Option[CreatureKindId] = {
    Selectors.getCreatureStats(s)(actor).missileLauncher.ammo
  }
}
