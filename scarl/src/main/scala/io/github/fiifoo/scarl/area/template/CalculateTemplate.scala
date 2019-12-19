package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.template.Template.Result
import io.github.fiifoo.scarl.world.WorldAssets

import scala.annotation.tailrec
import scala.util.Random

object CalculateTemplate {

  @tailrec
  def apply(assets: WorldAssets,
            area: Area,
            random: Random,
           )(template: Template): Result = {

    val result = try {
      Some(template(assets, Template.Context(area), random))
    } catch {
      case _: CalculateFailedException => None
    }

    if (result.isDefined) {
      result.get
    } else {
      CalculateTemplate(assets, area, random)(template)
    }
  }
}
