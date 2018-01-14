package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.ai.{Behavior, Intention, Priority, Tactic}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location

import scala.util.Random

case object TestMoveTactic extends Behavior {

  val intentions: List[(Intention, Priority.Value)] = List()

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    val from = actor(s).location
    val to = Location(from.x + 1, from.y)

    (this, TestMoveAction(to))
  }

  override def apply(s: State, actor: CreatureId, random: Random): Option[(Tactic, Action)] = {
    Some(behavior(s, actor, random))
  }
}
