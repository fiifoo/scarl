package io.github.fiifoo.scarl.game.area

import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.game.player.PlayerFov

object MapBuilder {

  def apply(fov: PlayerFov): Map[Location, MapLocation] = {
    fov.delta transform ((_, v) => MapLocation.apply(v))
  }

}
