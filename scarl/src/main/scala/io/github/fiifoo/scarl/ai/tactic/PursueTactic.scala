package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.core.action.Tactic
import io.github.fiifoo.scarl.core.action.Tactic.Result
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.{Line, Los}

import scala.util.Random

case class PursueTactic(target: SafeCreatureId, destination: Location) extends Tactic {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    target(s) flatMap (target => {
      val location = actor(s).location
      val line = Line(location, target.location)
      val range = actor(s).stats.sight.range

      if (line.size <= range + 1 && Los(s)(line)) {
        charge(s, actor, target, random)
      } else if (location != destination) {
        pursue(s, actor, target)
      } else {
        None
      }
    })
  }

  private def pursue(s: State, actor: CreatureId, target: Creature): Option[Result] = {
    Utils.move(s, actor, destination) map ((this, _))
  }

  private def charge(s: State, actor: CreatureId, target: Creature, random: Random): Option[Result] = {
    val tactic = ChargeTactic(SafeCreatureId(target.id), target.location)

    tactic(s, actor, random)
  }
}
