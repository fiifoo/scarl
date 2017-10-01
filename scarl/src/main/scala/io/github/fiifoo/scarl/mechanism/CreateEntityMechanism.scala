package io.github.fiifoo.scarl.mechanism

import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Machinery
import io.github.fiifoo.scarl.core.item.Mechanism
import io.github.fiifoo.scarl.core.kind.KindId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.CreateEntityEffect

case class CreateEntityMechanism(kind: KindId, disposable: Boolean, description: Option[String]) extends Mechanism {
  def interact(s: State, machinery: Machinery, control: Location): List[Effect] = {
    val effects = machinery.targets.toList map (CreateEntityEffect(kind, _, description))

    activate(machinery, effects)
  }
}
