package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.entity.ActorId
import io.github.fiifoo.scarl.core.mutation.ResetAddedActorsMutation

import scala.collection.mutable

class ActorQueue() {

  private val queue = mutable.PriorityQueue[(ActorId, Int)]()(Ordering.by(orderBy))

  def enqueueNewActors(s: State): State = {
    if (s.tmp.addedActors.nonEmpty) {
      s.tmp.addedActors foreach (actor => enqueue(actor, actor(s).tick))
      ResetAddedActorsMutation()(s)
    } else {
      s
    }
  }

  def enqueue(actorId: ActorId, number: Int): ActorQueue = {
    queue.enqueue((actorId, number))

    this
  }

  def dequeue(): ActorId = queue.dequeue()._1

  def dequeueAll: List[ActorId] = (queue map (_._1)).toList

  def isEmpty: Boolean = queue.isEmpty

  def nonEmpty: Boolean = queue.nonEmpty

  def head: ActorId = queue.head._1

  def headOption: Option[ActorId] = queue.headOption map (x => x._1)

  private def orderBy(x: (ActorId, Int)): (Int, Int) = (-x._2, -x._1.value)
}
