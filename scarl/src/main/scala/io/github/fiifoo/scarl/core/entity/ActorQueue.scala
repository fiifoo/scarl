package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State
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

  def isEmpty: Boolean = queue.isEmpty

  def head: ActorId = queue.head._1

  private def orderBy(x: (ActorId, Int)): (Int, Int) = (-x._2, -x._1.value)
}
