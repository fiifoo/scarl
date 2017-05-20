package io.github.fiifoo.scarl.game.map

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.game.PlayerFov

class MapBuilder(initial: Map[Location, MapLocation]) {

  private var map: Map[Location, MapLocation] = initial

  def apply(fov: PlayerFov): Unit = {
    val add = fov.delta mapValues MapLocation.apply

    map = map ++ add
  }

  def extract(next: Option[Map[Location, MapLocation]] = None): Map[Location, MapLocation] = {
    val m = map
    map = next getOrElse Map()

    m
  }
}
