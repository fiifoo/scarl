package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult, RemoveEntityEffect}
import io.github.fiifoo.scarl.core.entity.{Charge, EntityId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.ChargesMutation

case class ConsumeChargeEffect(entity: EntityId,
                               charge: Charge,
                               location: Location,
                               parent: Option[Effect] = None
                              ) extends Effect {

  def apply(s: State): EffectResult = {
    if (charge.charges > 1) {
      EffectResult(ChargesMutation(entity, charge.charges - 1))
    } else {
      charge.transformTo map (transformTo => {
        EffectResult(TransformEffect(entity, transformTo, description = charge.transformDescription, parent = Some(this)))
      }) getOrElse {
        EffectResult(RemoveEntityEffect(entity, Some(location), charge.transformDescription, Some(this)))
      }
    }
  }
}
