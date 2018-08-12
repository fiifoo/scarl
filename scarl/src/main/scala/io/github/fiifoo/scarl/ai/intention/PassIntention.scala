package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.action.PassAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case object PassIntention extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    Some((Utils.getTactic(s, actor), PassAction))
  }
}
