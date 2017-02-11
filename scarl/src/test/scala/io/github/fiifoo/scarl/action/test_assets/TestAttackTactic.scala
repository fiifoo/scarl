package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.AttackAction
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.{Rng, State}

case class TestAttackTactic(actor: CreatureId) extends Tactic {

  def apply(s: State, rng: Rng): (Tactic, Action, Rng) = {
    val targets = s.entities.values collect {
      case c: Creature if c.id != actor => c
    }

    (this, AttackAction(targets.head.id), rng)
  }
}
