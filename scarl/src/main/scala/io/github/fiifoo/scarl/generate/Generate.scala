package io.github.fiifoo.scarl.generate

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.mutation.SeedMutation

object Generate {
  def apply(): State = {
    val creatures = CreatureFactory().generate(State(), 100)
    val seeded = SeedMutation(creatures.seed + 1)(creatures)
    val walls = WallFactory().generate(seeded, 500)

    walls
  }
}
