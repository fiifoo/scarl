package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.entity.{Wall, WallId}
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}
import org.scalatest._

class FovSpec extends FlatSpec with Matchers {

  "Fov" should "calculate field of view" in {
    val wall = Wall(WallId(1), Location(0, 1))
    val state = NewEntityMutation(wall)(State())

    val fov = Fov(state) _

    fov(Location(0, 0), 1).size should ===(9)
    fov(Location(0, 0), 2).size should ===(24)
    fov(Location(0, 0), 3).size should ===(45)
    fov(Location(0, 0), 4).size should ===(74)
    fov(Location(0, 0), 5).size should ===(109)
  }

}
