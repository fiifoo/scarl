package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.effect.combat.ExplodeEffect

case class ExplodePower(description: Option[String] = None,
                        resources: Option[Resources] = None,
                        directed: Boolean = false,
                       ) extends CreaturePower with ItemPower {

  override def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    explode(s, usable, user)
  }

  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    explode(s, creature, user)
  }

  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    explode(s, item, user)
  }

  private def explode(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    val effect = getTarget(s, usable) map ((explosive: LocatableId, stats: Explosive) => {
      ExplodeEffect(
        explosive,
        getLocation(s, explosive, user),
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

  private def getLocation(s: State, explosive: LocatableId, user: Option[CreatureId]): Location = {
    val location = explosive(s).location

    if (this.directed) {
      user flatMap (user => {
        val candidate = if (user(s).location == location) {
          s.creature.trails.get(user) map (_.head)
        } else {
          Some(user(s).location)
        }

        candidate filter (_.adjacent.contains(location))
      }) getOrElse {
        location
      }
    } else {
      location
    }
  }
}
