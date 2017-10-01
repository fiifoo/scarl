package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.kind.KindId
import io.github.fiifoo.scarl.core.{Location, State}

case class CreateEntityEffect(kind: KindId,
                              location: Location,
                              description: Option[String] = None,
                              parent: Option[Effect] = None
                             ) extends Effect {

  def apply(s: State): EffectResult = {
    val mutations = kind(s).toLocation(s, s.idSeq, location).mutations

    EffectResult(mutations)
  }
}
