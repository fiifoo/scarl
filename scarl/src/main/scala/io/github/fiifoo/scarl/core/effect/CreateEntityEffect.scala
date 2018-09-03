package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.Kind.Options
import io.github.fiifoo.scarl.core.kind.KindId

case class CreateEntityEffect(kind: KindId,
                              location: Location,
                              owner: Option[CreatureId] = None,
                              description: Option[String] = None,
                              parent: Option[Effect] = None
                             ) extends Effect {

  def apply(s: State): EffectResult = {
    val mutations = kind(s).apply(s, s.idSeq, location, Options(owner)).mutations

    EffectResult(mutations)
  }
}
