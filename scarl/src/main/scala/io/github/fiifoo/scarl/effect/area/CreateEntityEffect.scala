package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult, LocalizedDescriptionEffect}
import io.github.fiifoo.scarl.core.kind.KindId
import io.github.fiifoo.scarl.core.{Location, State}

case class CreateEntityEffect(kind: KindId,
                              location: Location,
                              description: Option[String] = None,
                              parent: Option[Effect] = None
                             ) extends Effect with LocalizedDescriptionEffect {

  def apply(s: State): EffectResult = {
    val mutations = kind(s).toLocation(s, s.idSeq, location).mutations

    EffectResult(mutations)
  }
}
