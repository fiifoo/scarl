package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.theme.Theme
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

object TestTemplates {

  val iterations = 100

  def apply(assets: WorldAssets): Map[TemplateId, Int] = {
    val theme = assets.themes.values.head // Might be problem

    assets.templates mapValues test(assets, theme)
  }

  private def test(assets: WorldAssets, theme: Theme)(template: Template): Int = {
    val random = new Random(1)

    val passed = (0 until iterations).count(_ => {
      try {
        template(assets, theme, random)

        true
      } catch {
        case _: CalculateFailedException => false
      }
    })

    100 * passed / iterations
  }
}
