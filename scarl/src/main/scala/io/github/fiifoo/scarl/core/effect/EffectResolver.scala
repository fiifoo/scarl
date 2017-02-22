package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.mutation.RemoveEntitiesMutation

import scala.collection.mutable

class EffectResolver(listener: EffectListener = NullEffectListener) {

  def apply(s: State, effects: List[Effect]): State = {

    val queue = mutable.Queue[Effect]() ++= effects
    var _s = s

    while (queue.nonEmpty) {
      val effect = queue.dequeue()
      val result = effect(_s)

      result.mutations.foreach(mutation => _s = mutation(_s))
      result.effects.foreach(effect => queue.enqueue(effect))
      listener(_s, effect)
    }

    if (_s.tmp.removableEntities.nonEmpty) {
      _s = RemoveEntitiesMutation()(_s)
    }

    _s
  }
}
