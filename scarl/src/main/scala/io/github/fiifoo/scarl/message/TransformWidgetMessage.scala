package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.TransformWidgetEffect

class TransformWidgetMessage(player: () => CreatureId,
                             fov: () => Set[Location]
                            ) extends MessageBuilder[TransformWidgetEffect] {

  def apply(s: State, effect: TransformWidgetEffect): Option[String] = {
    effect.description flatMap (description => {
      if (fov() contains effect.widget(s).location) {
        Some(description)
      } else {
        None
      }
    })
  }
}
