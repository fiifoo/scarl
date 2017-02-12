package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

case class FactionId(value: String) {

  def apply(s: State): Faction = s.factions(this)
}
