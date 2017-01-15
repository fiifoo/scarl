package fi.fiifoo.scarl.core.test_assets

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.ActorId
import fi.fiifoo.scarl.core.mutation.ActorTickMutation

case class TestTickEffect(target: ActorId, amount: Int) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(ActorTickMutation(target, target(s).tick + amount))
  }
}
