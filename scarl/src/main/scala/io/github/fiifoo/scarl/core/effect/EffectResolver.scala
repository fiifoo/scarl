package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.State

object EffectResolver {

  def apply(s: State, effects: List[Effect]): (State, List[Effect]) = {
    (effects foldLeft(s, List[Effect]())) ((x, effect) => {
      val (s, prev) = x
      val (ns, resolved) = resolve(s, effect)

      (ns, prev ::: resolved)
    })
  }

  private def resolve(s: State, effect: Effect): (State, List[Effect]) = {
    val result = effect(s)
    val mutated = (result.mutations foldLeft s) ((s, mutation) => mutation(s))

    val (ns, resolved) = EffectResolver(mutated, result.effects)

    (ns, effect :: resolved)
  }
}
