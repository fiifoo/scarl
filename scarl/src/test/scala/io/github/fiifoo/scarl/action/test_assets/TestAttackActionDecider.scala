package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.AttackAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, ActionDecider}
import io.github.fiifoo.scarl.core.entity.Creature

object TestAttackActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    val creatures = s.entities filter (_._2.isInstanceOf[Creature]) map (_._2.asInstanceOf[Creature])
    val targets = creatures filterNot (_ == actor)

    AttackAction(targets.head)
  }
}
