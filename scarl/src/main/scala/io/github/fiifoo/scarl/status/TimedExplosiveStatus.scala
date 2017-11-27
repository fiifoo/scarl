package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.{Selectors, State}
import io.github.fiifoo.scarl.effect.area.ExplosiveTimerEffect
import io.github.fiifoo.scarl.effect.combat.ExplodeEffect

case class TimedExplosiveStatus(id: ActiveStatusId,
                                tick: Int,
                                target: ContainerId,
                                explodeAt: Int,
                               ) extends ActiveStatus {

  val interval = 100

  def setTick(tick: Int): TimedExplosiveStatus = copy(tick = tick)

  def apply(s: State): List[Effect] = {
    val tickEffect = TickEffect(id, interval)

    if (tick < explodeAt) {
      List(
        tickEffect,
        ExplosiveTimerEffect(
          explosive = target,
          location = target(s).location,
          timer = calculateTimer(s),
        )
      )
    } else {
      (Selectors.getWidgetItem(s)(target) flatMap (item => {
        item(s).explosive map (stats => {
          ExplodeEffect(
            explosive = target,
            location = target(s).location,
            stats = stats,
          )
        })
      })) map (List(tickEffect, _)) getOrElse List(tickEffect)
    }
  }

  private def calculateTimer(s: State): Int = {
    (explodeAt - s.tick) / interval
  }
}
