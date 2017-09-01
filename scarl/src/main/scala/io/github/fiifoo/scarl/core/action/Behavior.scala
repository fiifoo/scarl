package io.github.fiifoo.scarl.core.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

trait Behavior extends Tactic {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    Some(behavior(s, actor, random))
  }

  def behavior(s: State, actor: CreatureId, random: Random): Result
}