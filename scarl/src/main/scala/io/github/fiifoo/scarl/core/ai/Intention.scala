package io.github.fiifoo.scarl.core.ai

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

trait Intention {
  def apply(s: State, actor: CreatureId, random: Random): Option[Result]
}
