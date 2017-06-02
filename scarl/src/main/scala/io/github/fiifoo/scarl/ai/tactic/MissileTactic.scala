package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.{ExplodeAction, MoveAction, PassAction}
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.creature.Missile
import io.github.fiifoo.scarl.core.creature.Missile.Smart
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.{Line, Obstacle, Path}

import scala.util.Random

case class MissileTactic(actor: CreatureId,
                         destination: Location,
                         target: Option[SafeCreatureId]
                        ) extends Tactic {
  type Result = (Tactic, Action)

  def apply(s: State, random: Random): Result = {
    actor(s).missile flatMap (missile => {
      val from = actor(s).location

      if (from == destination) {
        Some(explode)
      } else {
        val nextDestination = getNextDestination(s, missile)
        val path = getPath(s, missile, from, nextDestination)

        path map (path => {
          val to = path(1)
          if (Obstacle.movement(s)(to).isDefined) {
            explode
          } else {
            move(to, nextDestination)
          }
        })
      }
    }) getOrElse {
      (this, PassAction())
    }
  }

  private def explode: Result = {
    (this, ExplodeAction)
  }

  private def move(to: Location, nextDestination: Location): Result = {
    val tactic = this.copy(destination = nextDestination)
    val action = MoveAction(to)

    (tactic, action)
  }

  private def getNextDestination(s: State, missile: Missile): Location = {
    if (missile.guidance.isDefined) {
      getTargetLocation(s) getOrElse destination
    } else {
      destination
    }
  }

  private def getPath(s: State, missile: Missile, from: Location, to: Location): Option[Vector[Location]] = {
    missile.guidance collect {
      case Smart => Path(s)(from, to)
    } getOrElse {
      Some(Line(from, to))
    }
  }

  private def getTargetLocation(s: State): Option[Location] = {
    target flatMap (_ (s) map (_.location))
  }
}
