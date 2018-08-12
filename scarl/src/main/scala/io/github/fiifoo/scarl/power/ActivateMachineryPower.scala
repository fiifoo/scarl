package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity.Selectors.getItemLocation
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.effect.interact.ActivateMachineryEffect

case class ActivateMachineryPower(description: Option[String] = None,
                                  resources: Option[Resources] = None,
                                 ) extends CreaturePower with ItemPower {
  override def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    usable match {
      case creature: CreatureId => this.apply(s, creature, user)
      case item: ItemId => this.apply(s, item, user)
    }
  }

  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    activate(s, user)(creature(s).location)
  }

  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    getItemLocation(s)(item) map (activate(s, user)) getOrElse List()
  }

  private def activate(s: State, user: Option[CreatureId])(location: Location): List[Effect] = {
    s.index.locationMachinery.get(location) flatMap (machinery => {
      user map (user => {
        machinery.toList map (machinery => {
          ActivateMachineryEffect(user, location, machinery)
        })
      })
    }) getOrElse {
      List()
    }
  }
}
