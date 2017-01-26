package io.github.fiifoo.scarl.ai

import io.github.fiifoo.scarl.action.{AttackAction, MoveAction, PassAction}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, ActionDecider => ActionDeciderTrait}
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.geometry.Line

object ActionDecider extends ActionDeciderTrait {

  def apply(s: State, actor: Creature): Action = {
    val enemy = SeekEnemy(s, actor)

    if (enemy.isDefined) {
      attack(actor, enemy.get)
    } else {
      PassAction()
    }
  }

  private def attack(actor: Creature, enemy: Creature): Action = {
    val line = Line(actor.location, enemy.location)
    if (line.size <= 2) {
      AttackAction(enemy)
    } else {
      MoveAction(line(1))
    }
  }
}
