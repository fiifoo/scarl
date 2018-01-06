package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.PassAction
import io.github.fiifoo.scarl.ai.SeekEnemy
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Tactic
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork

import scala.util.Random

case class FollowTactic(target: SafeCreatureId) extends Tactic {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    target(s) flatMap (target => {
      val enemy = SeekEnemy(s, actor)

      enemy flatMap (enemy => {
        charge(s, actor, enemy, random)
      }) orElse {
        follow(s, actor, target) orElse Some((this, PassAction))
      }
    })
  }

  private def follow(s: State, actor: CreatureId, target: Creature): Option[Result] = {
    if (WaypointNetwork.isNearbyLocation(s, actor(s).location, target.location)) {
      Utils.move(s, actor, target.location, displace = true) map ((this, _))
    } else {
      None
    }
  }

  private def charge(s: State, actor: CreatureId, target: Creature, random: Random): Option[Result] = {
    val tactic = ChargeTactic(SafeCreatureId(target.id), target.location)

    tactic(s, actor, random)
  }
}
