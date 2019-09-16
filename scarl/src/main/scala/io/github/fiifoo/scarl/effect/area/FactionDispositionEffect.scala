package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Faction.Disposition
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.mutation.FactionDispositionMutation

case class FactionDispositionEffect(faction: FactionId,
                                    target: FactionId,
                                    disposition: Option[Disposition],
                                    parent: Option[Effect] = None
                                   ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(FactionDispositionMutation(this.faction, this.target, this.disposition))
  }
}
