package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.entity.CreatureId

case class CreatureDamageMutation(creature: CreatureId, damage: Int) extends Mutation {

  def apply(s: State): State = {
    val mutated = creature(s).copy(damage = damage)

    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
