package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Condition
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.creature.condition.RollConditionsEffect

case class RollUserConditionPower(description: Option[String] = None,
                                  resources: Option[Resources] = None,
                                  condition: Condition,
                                 ) extends CreaturePower with ItemPower {
  override def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    roll(s, user)
  }

  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    roll(s, user)
  }

  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    roll(s, user)
  }

  private def roll(s: State, user: Option[CreatureId]): List[Effect] = {
    user map (user => {
      List(
        RollConditionsEffect(user, List(this.condition), user(s).location)
      )
    }) getOrElse {
      Nil
    }
  }
}
