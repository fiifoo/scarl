package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.AttackAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Behavior
import io.github.fiifoo.scarl.core.action.Tactic.Result
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}

import scala.util.Random

case object TestAttackTactic extends Behavior {

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    val targets = s.entities.values collect {
      case c: Creature if c.id != actor => c
    }

    (this, AttackAction(targets.head.id))
  }
}
