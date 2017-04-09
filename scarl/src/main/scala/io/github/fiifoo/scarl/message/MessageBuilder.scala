package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect

abstract class MessageBuilder[E <: Effect] {
  def apply(s: State, effect: E): Option[String]
}
