package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

object TestTemplates {

  val iterations = 100

  def apply(assets: WorldAssets): Map[TemplateId, Int] = {
    val area = assets.areas.values.head // Might be problem

    assets.templates mapValues test(assets, area)
  }

  private def test(assets: WorldAssets, area: Area)(template: Template): Int = {
    val random = new Random(1)

    val passed = (0 until iterations).count(_ => {
      try {
        template(assets, area, random)

        true
      } catch {
        case _: CalculateFailedException => false
      }
    })

    100 * passed / iterations
  }
}
