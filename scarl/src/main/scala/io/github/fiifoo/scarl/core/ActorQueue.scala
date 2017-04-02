package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.entity.ActorId
import io.github.fiifoo.scarl.core.mutation.{ResetAddedActorsMutation, ResetStoredActorsMutation}

import scala.collection.mutable

class ActorQueue() {

  private val queue = mutable.PriorityQueue[(ActorId, Int)]()(Ordering.by(orderBy))

  def enqueueNewActors(s0: State): State = {
    val s1 = if (s0.tmp.addedActors.nonEmpty) {
      s0.tmp.addedActors foreach (actor => enqueue(actor, actor(s0).tick))
      ResetAddedActorsMutation()(s0)
    } else {
      s0
    }

    if (s1.stored.actors.nonEmpty) {
      s1.stored.actors foreach (actor => enqueue(actor, actor(s1).tick))
      ResetStoredActorsMutation()(s1)
    } else {
      s1
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
