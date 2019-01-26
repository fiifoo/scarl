package io.github.fiifoo.scarl.world.system

import io.github.fiifoo.scarl.world.system.source.StellarBodySourceId

object StellarBody {
  type Id = String
}

case class StellarBody(id: StellarBody.Id,
                       source: StellarBodySourceId,
                       mass: Double,
                       position: Position,
                       speed: Vector,
                      ) {

  def tick(tick: Int, bodies: Iterable[StellarBody]): StellarBody = {
    val acceleration = Physics.gravity(bodies, this)
    val speed = this.speed.add(acceleration.multiply(tick))
    val position = this.position.add(speed.multiply(tick))

    this.copy(speed = speed, position = position)
  }
}
