package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Behavior
import io.github.fiifoo.scarl.core.action.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case object FollowerTactic extends Behavior {

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    actor(s).owner flatMap (owner => {
      FollowTactic(owner)(s, actor, random)
    }) getOrElse {
      RoamTactic.behavior(s, actor, random)
    }
  }
}
