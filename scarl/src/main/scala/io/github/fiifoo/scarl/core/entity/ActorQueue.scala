package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.ActorQueue._

import scala.collection.SortedSet

object ActorQueue {
  type Member = (ActorId, Int)

  implicit val ordering = Ordering.by(orderBy)

  private def orderBy(x: (ActorId, Int)): (Int, Int) = (x._2, x._1.value)

  def apply(s: State): ActorQueue = {
    val queue = (s.entities.values collect {
      case actor: Actor => (actor.id, actor.tick)
    }).to[SortedSet]

    ActorQueue(queue)
  }
}

case class ActorQueue(queue: SortedSet[Member] = SortedSet[Member]()) {

  def enqueue(actor: Actor): ActorQueue = {
    this.copy(queue + (actor.id -> actor.tick))
  }

  def dequeue: Option[(ActorId, ActorQueue)] = {
    queue.headOption map (member => {
      val actor = member._1
      val next = queue - member

      (actor, this.copy(next))
    })
  }

  def remove(actor: Actor): ActorQueue = {
    this.copy(queue - (actor.id -> actor.tick))
  }

  def isEmpty: Boolean = queue.isEmpty

  def nonEmpty: Boolean = queue.nonEmpty

  def headOption: Option[ActorId] = queue.headOption map (_._1)
}
