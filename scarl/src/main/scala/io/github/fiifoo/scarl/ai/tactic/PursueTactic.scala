package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.MoveAction
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.{Line, Los, Path}

import scala.util.Random

case class PursueTactic(actor: CreatureId,
                        target: SafeCreatureId,
                        destination: Location
                       ) extends Tactic {
  type Result = (Tactic, Action)

  def apply(s: State, random: Random): Result = {
    target(s) flatMap (target => {
      pursueOrCharge(s, random, target)
    }) getOrElse {
      roam(s, random)
    }
  }

  private def pursueOrCharge(s: State, random: Random, target: Creature): Option[Result] = {
    val location = actor(s).location
    val line = Line(location, target.location)
    val range = actor(s).stats.sight.range

    if (line.size <= range + 1 && Los(s)(line)) {
      charge(s, random, target)
    } else if (location != destination) {
      pursue(s, target)
    } else {
      None
    }
  }

  private def pursue(s: State, target: Creature): Option[Result] = {
    Path(s)(actor(s).location, destination) map (path => {
      val action = MoveAction(path.head)

      (this, action)
    })
  }

  private def charge(s: State, random: Random, target: Creature): Option[Result] = {
    val tactic = ChargeTactic(actor, SafeCreatureId(target.id), target.location)

    Some(tactic(s, random))
  }

  private def roam(s: State, random: Random): Result = {
    RoamTactic(actor)(s, random)
  }
}
