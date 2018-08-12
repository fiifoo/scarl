package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.combat.ExplodeEffect

case class ExplodePower(description: Option[String] = None,
                        resources: Option[Resources] = None,
                       ) extends CreaturePower with ItemPower {

  override def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    explode(s, usable)
  }

  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    explode(s, creature)
  }

  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    explode(s, item)
  }

  private def explode(s: State, usable: UsableId): List[Effect] = {
    val effect = getTarget(s, usable) map ((explosive: LocatableId, stats: Explosive) => {
      ExplodeEffect(
        explosive,
        explosive(s).location,
        stats
      )
    }).tupled

    effect map (List(_)) getOrElse List()
  }

  private def getTarget(s: State, usable: UsableId): Option[(LocatableId, Explosive)] = {
    usable match {
      case item: ItemId =>
        val container = item(s).container match {
          case container: ContainerId => Some(container)
          case _ => None
        }
        val stats = item(s).explosive

        container flatMap (container => {
          stats map (stats => {
            (container, stats)
          })
        })
      case creature: CreatureId =>
        Some(creature, Selectors.getCreatureStats(s)(creature).explosive)
    }
  }
}
