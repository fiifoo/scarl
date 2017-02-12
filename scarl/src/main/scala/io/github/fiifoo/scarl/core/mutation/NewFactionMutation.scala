package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.entity._

case class NewFactionMutation(faction: Faction) extends Mutation {

  def apply(s: State): State = {
    s.copy(
      factions = s.factions + (faction.id -> faction)
    )
  }
}
