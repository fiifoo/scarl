package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Faction.Disposition
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.area.FactionDispositionEffect

case class FactionDispositionPower(description: Option[String] = None,
                                   resources: Option[Resources] = None,
                                   disposition: Disposition,
                                  ) extends CreaturePower {
  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    user map (user => {
      List(
        FactionDispositionEffect(user(s).faction, creature(s).faction, Some(this.disposition)),
        FactionDispositionEffect(creature(s).faction, user(s).faction, Some(this.disposition)),
      )
    }) getOrElse {
      List()
    }
  }
}
