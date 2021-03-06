package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}

import scala.util.Random

case class FollowIntention(target: SafeCreatureId, waiting: Boolean = false) extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    target(s) flatMap Utils.follow(s, actor, !waiting)
  }
}
