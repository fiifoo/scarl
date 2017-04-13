package io.github.fiifoo.scarl.core.character

import io.github.fiifoo.scarl.core.character.Progression.Step
import io.github.fiifoo.scarl.core.entity.Creature

object Progression {

  case class Requirements(experience: Int)

  case class Step(requirements: Requirements, stats: Creature.Stats)

}

case class Progression(id: ProgressionId, steps: Vector[Step])
