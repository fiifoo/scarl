package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.State

import scala.collection.mutable

object EffectResolver {

  def apply(state: State, effects: List[Effect]): (State, List[Effect]) = {

    val queue = mutable.Queue[Effect]() ++= effects
    var resolved: List[Effect] = List()
    var s = state

    while (queue.nonEmpty) {
      val effect = queue.dequeue()
      val result = effect(s)

      result.mutations.foreach(mutation => s = mutation(s))
      result.effects.foreach(effect => queue.enqueue(effect))
      resolved = effect :: resolved
    }

    (s, resolved)
  }
}
