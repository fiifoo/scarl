package io.github.fiifoo.scarl.core.character

import io.github.fiifoo.scarl.core.character.Progression.Step

object Progression {

  case class Requirements(experience: Int)

  case class Step(requirements: Requirements, stats: Stats)

}

case class Progression(id: ProgressionId, steps: Vector[Step])
