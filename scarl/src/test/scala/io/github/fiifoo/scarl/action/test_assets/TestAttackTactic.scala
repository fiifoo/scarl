package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.AttackAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}

case class TestAttackTactic(actor: CreatureId) extends Tactic {

  def apply(s: State): (Tactic, Action) = {
    val creatures = s.entities filter (_._2.isInstanceOf[Creature]) map (_._2.asInstanceOf[Creature])
    val targets = creatures filterNot (_.id == actor)

    (this, AttackAction(targets.head))
  }
}
