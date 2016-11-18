package fi.fiifoo.scarl.action.test_assets

import fi.fiifoo.scarl.action.AttackAction
import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.action.{Action, ActionDecider}
import fi.fiifoo.scarl.core.entity.Creature

object TestAttackActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    val creatures = s.entities filter (_._2.isInstanceOf[Creature]) map (_._2.asInstanceOf[Creature])
    val targets = creatures filterNot (_ == actor)

    AttackAction(targets.head)
  }
}
