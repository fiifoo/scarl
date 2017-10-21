package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.template.Template.Result
import io.github.fiifoo.scarl.area.theme.Theme
import io.github.fiifoo.scarl.world.WorldAssets

import scala.annotation.tailrec
import scala.util.Random

object CalculateTemplate {

  @tailrec
  def apply(assets: WorldAssets,
            theme: Theme,
            random: Random,
           )(template: Template): Result = {

    val result = try {
      Some(template(assets, theme, random))
    } catch {
      case _: CalculateFailedException => None
    }

    if (result.isDefined) {
      result.get
    } else {
      CalculateTemplate(assets, theme, random)(template)
    }
  }
}
