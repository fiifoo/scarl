package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.core.creature.Faction.Disposition
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.game.RunState

object FactionInfo {
  def apply(state: RunState): FactionInfo = {
    FactionInfo(state.instance.factions.dispositions)
  }
}

case class FactionInfo(dispositions: Map[FactionId, Map[FactionId, Disposition]] = Map())
