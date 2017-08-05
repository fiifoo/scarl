package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.ShootEffect

case class ShootAction(location: Location) extends Action {
  val cost = 100

  def apply(s: State, actor: CreatureId): List[Effect] = {
    List(
      TickEffect(actor, cost),
      ShootEffect(actor, location)
    )
  }
}
