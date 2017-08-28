package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.LocatableId
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.{Location, State}

case class SummonCreatureEffect(kind: CreatureKindId,
                                location: Location,
                                source: LocatableId,
                                parent: Option[Effect] = None
                               ) extends Effect {

  def apply(s: State): EffectResult = {
    val result = kind(s).toLocation(s, s.idSeq, location)

    EffectResult(result.mutations)
  }
}
