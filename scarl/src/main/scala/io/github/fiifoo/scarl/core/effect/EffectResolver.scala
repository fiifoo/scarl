package fi.fiifoo.scarl.core.effect

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.mutation.RemoveEntitiesMutation

import scala.collection.mutable

object EffectResolver {

  def apply(s: State, effects: List[Effect]): State = {

    val queue = mutable.Queue[Effect]() ++= effects
    var _s = s

    while (queue.nonEmpty) {
      val effect = queue.dequeue()
      val result = effect(_s)

      result.mutations.foreach(mutation => _s = mutation(_s))
      result.effects.foreach(effect => queue.enqueue(effect))
    }

    if (_s.tmp.removableEntities.nonEmpty) {
      _s = RemoveEntitiesMutation()(_s)
    }

    _s
  }
}
