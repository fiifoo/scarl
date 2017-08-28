package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.entity.{Wall, WallId}
import io.github.fiifoo.scarl.core.kind.WallKindId
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{Location, State}
import org.scalatest._

class FovSpec extends FlatSpec with Matchers {

  "Fov" should "calculate field of view" in {

    /*
    ..............
    ..###.........
    ..#@..........
    ..###.........
    ..............
     */
    val walls = Set(
      Location(1, -1),
      Location(0, -1),
      Location(-1, -1),
      Location(-1, 0),
      Location(-1, 1),
      Location(0, 1),
      Location(1, 1)
    )

    val state = walls.foldLeft(State())((s, location) => {
      val (nextId, nextIdSeq) = s.idSeq()
      val wall = Wall(WallId(nextId), WallKindId("wall"), location)

      NewEntityMutation(wall)(IdSeqMutation(nextIdSeq)(s))
    })

    val fov = Fov(state) _

    /*
    ..............
    ..xxx.........
    ..x@x.........
    ..xxx.........
    ..............
     */
    fov(Location(0, 0), 1).size should ===(9)


    /*
    ..............
    ..xxxx........
    ..x@xx........
    ..xxxx........
    ..............
     */
    fov(Location(0, 0), 2).size should ===(12)


    /*
    ..............
    ......x.......
    ..xxxxx.......
    ..x@xxx.......
    ..xxxxx.......
    ......x.......
    ..............
     */
    fov(Location(0, 0), 3).size should ===(17)

    /*
    ..............
    ......xx......
    ..xxxxxx......
    ..x@xxxx......
    ..xxxxxx......
    ......xx......
    ..............
     */
    fov(Location(0, 0), 4).size should ===(22)

    /*
    ..............
    ........x.....
    ......xxx.....
    ..xxxxxxx.....
    ..x@xxxxx.....
    ..xxxxxxx.....
    ......xxx.....
    ........x.....
    ..............
     */
    fov(Location(0, 0), 5).size should ===(29)
  }
}
