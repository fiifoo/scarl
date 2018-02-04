package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case object FollowOwnerIntention extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    actor(s).owner flatMap (_ (s)) flatMap Utils.follow(s, actor)
  }
}
