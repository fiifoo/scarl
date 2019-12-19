package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.Utils.TemplateMock
import io.github.fiifoo.scarl.area.template.Template
import io.github.fiifoo.scarl.area.theme.ThemeId
import io.github.fiifoo.scarl.world.WorldAssets
import org.scalatest._

import scala.util.Random

class BurrowFeatureSpec extends FlatSpec with Matchers {

  private val assets = WorldAssets()
  private val theme = ThemeId("")

  "BurrowFeature" should "burrow walls" in {
    val source = TemplateMock(
      """
        |###############
        |###############
        |#######.#######
        |###############
        |###############
      """.stripMargin.trim
    )

    val result = burrow(source, 20, 30, 30)

    result.sketch.trim should ===(
      """
        |###############
        |######...#.####
        |##...#.....####
        |####.......####
        |###############
      """.stripMargin.trim)
  }

  private def burrow(template: TemplateMock, min: Int, max: Int, noise: Int): TemplateMock = {
    val result = BurrowFeature(min, max, noise)(
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
