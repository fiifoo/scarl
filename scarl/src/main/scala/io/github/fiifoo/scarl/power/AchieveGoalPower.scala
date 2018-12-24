package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.world.GoalId
import io.github.fiifoo.scarl.effect.creature.AchieveGoalEffect

case class AchieveGoalPower(description: Option[String] = None,
                            resources: Option[Resources] = None,
                            goal: GoalId,
                           ) extends CreaturePower with ItemPower {
  override def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    achieve
  }

  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    achieve
  }

  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    achieve
  }

  private def achieve: List[Effect] = {
    List(
      AchieveGoalEffect(this.goal)
    )
  }
}
