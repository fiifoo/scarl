package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId

trait Stance {
  val key: String
  val duration: Int

  def modifyStats(stats: Stats): Stats

  def effects(s: State, creature: CreatureId): List[Effect]
}
