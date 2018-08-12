package io.github.fiifoo.scarl.mechanism

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, RemoveEntityEffect}
import io.github.fiifoo.scarl.core.entity.Machinery
import io.github.fiifoo.scarl.core.entity.Selectors.getLocationWidgets
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Mechanism

case class RemoveWidgetMechanism(description: Option[String],
                                 disposable: Boolean,
                                 removeDescription: Option[String],
                                ) extends Mechanism {
  def interact(s: State, machinery: Machinery, control: Location): List[Effect] = {
    val widgets = machinery.targets flatMap getLocationWidgets(s)
    val effects = (widgets map (widget => {
      RemoveEntityEffect(widget, Some(widget(s).location), removeDescription)
    })).toList

    activate(machinery, effects)
  }
}
