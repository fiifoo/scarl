package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.Utils.TemplateMock
import io.github.fiifoo.scarl.area.template.ContentSelection.{FixedDoor, FixedWall}
import io.github.fiifoo.scarl.area.template.Template
import io.github.fiifoo.scarl.area.theme.ThemeId
import io.github.fiifoo.scarl.core.kind.{ItemKindId, WallKindId}
import io.github.fiifoo.scarl.world.WorldAssets
import org.scalatest._

import scala.util.Random

class HouseFeatureSpec extends FlatSpec with Matchers {

  private val assets = WorldAssets()
  private val theme = ThemeId("")
  private val wall = FixedWall(WallKindId(""))
  private val door = FixedDoor(ItemKindId(""))

  "HouseFeature" should "build walls" in {
    val source = TemplateMock(
      """
        |#############################
        |#...........................#
        |#...........................#
        |#...........................#
        |#...........................#
        |#...........................#
        |#...........................#
        |#...........................#
        |#############################
      """.stripMargin.trim
    )

    val result = build(source, 4, 3, 10)

    result.sketch.trim should ===(
      """
        |#############################
        |#.............../...#...#...#
        |#...............#...#.../...#
        |#...............#...#...#...#
        |########/######/#########/###
        |#......#./.......#..#../....#
        |#....../.#......./../..#....#
        |#......#.#.......#..#..#....#
        |#############################
      """.stripMargin.trim)
  }

  private def build(template: TemplateMock, iterations: Int, roomSize: Int, doorFactor: Int): TemplateMock = {
    val result = HouseFeature(iterations, roomSize, doorFactor, Some(wall), Some(door))(
      assets,
      Template.Context(ThemeId("")),
      template.shape,
      template.content,
      template.locations,
      Set(),
      Set(),
      new Random(1)
    )

    template.copy(content = result)
  }
}
