package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.entity.{Wall, WallId}
import io.github.fiifoo.scarl.core.kind.WallKindId
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}
import org.scalatest._

class PathSpec extends FlatSpec with Matchers {

  "Path" should "find path from one location to another" in {
    val New = NewEntityMutation

    val w1 = Wall(WallId(1), WallKindId("wall"), Location(3, 0))
    val s1 = New(w1)(State())

    Path(s1)(Location(0, 0), Location(0, 0)) should ===(None)
    Path(s1)(Location(0, 0), Location(3, 0)) should ===(Some(Vector(
      Location(1, 0),
      Location(2, 0),
      Location(3, 0)
    )))
    Path(s1)(Location(0, 0), Location(4, 0)) should ===(Some(Vector(
      Location(1, 0),
      Location(2, 0),
      Location(3, 1),
      Location(4, 0)
    )))
    Path(s1)(Location(3, 1), Location(4, 0)) should ===(Some(Vector(Location(4, 0))))

    val w2 = Wall(WallId(2), WallKindId("wall"), Location(3, 1))
    val w3 = Wall(WallId(3), WallKindId("wall"), Location(3, 2))
    val w4 = Wall(WallId(4), WallKindId("wall"), Location(3, -1))
    val w5 = Wall(WallId(5), WallKindId("wall"), Location(3, -2))
    val w6 = Wall(WallId(6), WallKindId("wall"), Location(3, -3))

    val s2 = New(w6)(New(w5)(New(w4)(New(w3)(New(w2)(s1)))))

    Path(s2)(Location(0, 0), Location(4, 0)) should ===(Some(Vector(
      Location(1, 1),
      Location(2, 2),
      Location(3, 3),
      Location(4, 2),
      Location(4, 1),
      Location(4, 0)
    )))
  }

  it should "not find path if there isn't one" in {
    val New = NewEntityMutation

    val w1 = Wall(WallId(1), WallKindId("wall"), Location(-1, -1))
    val w2 = Wall(WallId(2), WallKindId("wall"), Location(-1, 0))
    val w3 = Wall(WallId(3), WallKindId("wall"), Location(-1, 1))
    val w4 = Wall(WallId(4), WallKindId("wall"), Location(0, 1))
    val w5 = Wall(WallId(5), WallKindId("wall"), Location(1, 1))
    val w6 = Wall(WallId(6), WallKindId("wall"), Location(1, 0))
    val w7 = Wall(WallId(7), WallKindId("wall"), Location(1, -1))
    val w8 = Wall(WallId(8), WallKindId("wall"), Location(0, -1))

    val s = New(w8)(New(w7)(New(w6)(New(w5)(New(w4)(New(w3)(New(w2)(New(w1)(State()))))))))

    Path(s)(Location(0, 0), Location(5, 0)) should ===(None)
    Path(s)(Location(5, 0), Location(0, 0)) should ===(None)
  }
}
