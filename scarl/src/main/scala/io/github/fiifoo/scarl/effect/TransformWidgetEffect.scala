package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.ContainerId
import io.github.fiifoo.scarl.core.kind.WidgetKindId
import io.github.fiifoo.scarl.core.mutation.{NewEntityMutation, RemovableEntityMutation}

case class TransformWidgetEffect(widget: ContainerId,
                                 to: WidgetKindId,
                                 description: Option[String] = None
                                ) extends Effect {

  def apply(s: State): EffectResult = {
    val (container, item, status) = to(s)(s, widget(s).location)

    EffectResult(List(
      NewEntityMutation(container),
      NewEntityMutation(item),
      NewEntityMutation(status),
      RemovableEntityMutation(widget)
    ))
  }
}
