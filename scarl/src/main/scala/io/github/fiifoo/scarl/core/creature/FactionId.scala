package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.State

case class FactionId(value: String) {

  def apply(s: State): Faction = s.assets.factions(this)
}
