package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.ActorId

case class ActorTickMutation(actor: ActorId, tick: Int) extends Mutation {

  def apply(s: State): State = {
    val mutated = actor(s).setTick(tick)

    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
