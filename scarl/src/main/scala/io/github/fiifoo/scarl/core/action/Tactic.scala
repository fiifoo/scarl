package io.github.fiifoo.scarl.core.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

trait Tactic {
  val actor: CreatureId

  def apply(s: State, random: Random): (Tactic, Action)
}
