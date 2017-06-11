package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.KindId
import io.github.fiifoo.scarl.core.{Location, State}

case class TransformedEffect(from: EntityId,
                             to: KindId,
                             location: Location,
                             owner: Option[CreatureId] = None,
                             description: Option[String] = None,
                             parent: Option[Effect] = None
                            ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
