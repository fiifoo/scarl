package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.MoveAction
import io.github.fiifoo.scarl.ai.SeekEnemy
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Tactic.Result
import io.github.fiifoo.scarl.core.action.{PassAction, Tactic}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.geometry.Path

import scala.util.Random

case class FollowTactic(target: SafeCreatureId) extends Tactic {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    target(s) flatMap (target => {
      val enemy = SeekEnemy(s, actor(s))

      enemy flatMap (enemy => {
        charge(s, actor, enemy, random)
      }) orElse {
        follow(s, actor, target) orElse Some((this, PassAction))
      }
    })
  }

  private def follow(s: State, actor: CreatureId, target: Creature): Option[Result] = {
    Path(s)(actor(s).location, target.location) map (path => {
      val action = MoveAction(path.head)

      (this, action)
    })
  }

  private def charge(s: State, actor: CreatureId, target: Creature, random: Random): Option[Result] = {
    val tactic = ChargeTactic(SafeCreatureId(target.id), target.location)

    tactic(s, actor, random)
  }
}
