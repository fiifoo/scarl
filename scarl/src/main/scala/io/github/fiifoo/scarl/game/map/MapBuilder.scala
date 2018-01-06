package io.github.fiifoo.scarl.game.map

import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.game.player.PlayerFov

object MapBuilder {

  def apply(fov: PlayerFov): Map[Location, MapLocation] = {
    fov.delta mapValues MapLocation.apply
  }

}
