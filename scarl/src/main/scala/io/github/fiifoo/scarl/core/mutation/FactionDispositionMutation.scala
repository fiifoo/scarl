package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Faction.Disposition
import io.github.fiifoo.scarl.core.creature.FactionId

case class FactionDispositionMutation(faction: FactionId, target: FactionId, disposition: Option[Disposition]) extends Mutation {

  def apply(s: State): State = {
    val current = s.factions.dispositions.getOrElse(faction, Map())

    val next = disposition.map(disposition => {
      current + (target -> disposition)
    }) getOrElse {
      current - target
    }

    s.copy(factions = s.factions.copy(
      dispositions = s.factions.dispositions + (faction -> next)
    ))
  }
}
