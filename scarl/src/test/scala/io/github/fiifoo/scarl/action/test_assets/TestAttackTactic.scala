package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.AttackAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}

import scala.util.Random

case class TestAttackTactic(actor: CreatureId) extends Tactic {

  def apply(s: State, random: Random): (Tactic, Action) = {
    val targets = s.entities.values collect {
      case c: Creature if c.id != actor => c
    }

    (this, AttackAction(targets.head.id))
  }
}
