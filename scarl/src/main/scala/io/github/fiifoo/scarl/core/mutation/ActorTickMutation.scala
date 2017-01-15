package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.entity.ActorId

case class ActorTickMutation(actor: ActorId, tick: Int) extends Mutation {

  def apply(s: State): State = {
    val mutated = actor(s).setTick(tick)

    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
