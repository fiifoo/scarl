package fi.fiifoo.scarl.core.entity

trait Actor extends Entity {
  val id: ActorId
  val tick: Int

  def setTick(tick: Int): Actor
}
