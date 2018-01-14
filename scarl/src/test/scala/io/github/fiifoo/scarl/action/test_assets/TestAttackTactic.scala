package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.AttackAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.ai.{Behavior, Intention, Priority, Tactic}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}

import scala.util.Random

case object TestAttackTactic extends Behavior {

  val intentions: List[(Intention, Priority.Value)] = List()

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    val targets = s.entities.values collect {
      case c: Creature if c.id != actor => c
    }

    (this, AttackAction(targets.head.id))
  }

  override def apply(s: State, actor: CreatureId, random: Random): Option[(Tactic, Action)] = {
    Some(behavior(s, actor, random))
  }
}
