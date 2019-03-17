package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId

trait Condition {
  val key: String
  val strength: Int

  def modifyStats(stats: Stats, strength: Int): Stats

  def resistance(stats: Stats): Int

  def effects(s: State, creature: CreatureId, strength: Int): List[Effect]
}
