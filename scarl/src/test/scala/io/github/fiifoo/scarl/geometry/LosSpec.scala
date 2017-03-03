package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.entity.{Wall, WallId}
import io.github.fiifoo.scarl.core.kind.WallKindId
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}
import org.scalatest._

class LosSpec extends FlatSpec with Matchers {

  "Los" should "check line of sight from one location to another" in {
    val wall = Wall(WallId(1), WallKindId("wall"), Location(0, 1))
    val state = NewEntityMutation(wall)(State())

    val los = Los(state) _

    los(Line(Location(0, 0), Location(1, 0))) should ===(true)
    los(Line(Location(0, 0), Location(2, 0))) should ===(true)
    los(Line(Location(0, 0), Location(0, 1))) should ===(true)
    los(Line(Location(0, 0), Location(0, 2))) should ===(false)
  }
}
