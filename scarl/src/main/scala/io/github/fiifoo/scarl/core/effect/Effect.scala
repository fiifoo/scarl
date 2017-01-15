package fi.fiifoo.scarl.core.effect

import fi.fiifoo.scarl.core.State

trait Effect {
  def apply(s: State): EffectResult
}
