package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Selectors.getItemLocation
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId, ItemPower}
import io.github.fiifoo.scarl.effect.interact.ActivateMachineryEffect

case class ActivateMachineryPower(useDescription: Option[String] = None) extends ItemPower {

  def apply(s: State, item: ItemId, user: Option[CreatureId] = None): List[Effect] = {
    getItemLocation(s)(item) flatMap (location => {
      s.index.locationMachinery.get(location) flatMap (machinery => {
        user map (user => {
          machinery.toList map (machinery => {
            ActivateMachineryEffect(user, location, machinery)
          })
        })
      })
    }) getOrElse {
      List()
    }
  }
}