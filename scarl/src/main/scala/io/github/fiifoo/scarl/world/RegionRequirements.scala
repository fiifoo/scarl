package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.core.world.GoalId

case class RegionRequirements(goals: List[Set[GoalId]] = List()) {
  def apply(world: WorldState): Boolean = {
    this.checkGoals(world)
  }

  private def checkGoals(world: WorldState): Boolean = {
    !(this.goals exists (goals => !(goals exists world.goals.contains)))
  }
}
