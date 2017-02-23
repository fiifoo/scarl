package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.effect.TransformWidgetEffect
import io.github.fiifoo.scarl.game.Player

object TransformWidgetMessage {

  def apply(s: State, effect: TransformWidgetEffect, player: Player): Option[String] = {
    effect.description flatMap (description => {
      if (player.fov contains effect.widget(s).location) {
        Some(description)
      } else {
        None
      }
    })
  }
}
