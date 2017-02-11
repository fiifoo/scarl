package io.github.fiifoo.scarl.generate

import io.github.fiifoo.scarl.core.State

object Generate {
  def apply(): State = {
    val creatures = CreatureFactory().generate(State(), 100)
    val walls = WallFactory().generate(creatures, 500)

    walls
  }
}
