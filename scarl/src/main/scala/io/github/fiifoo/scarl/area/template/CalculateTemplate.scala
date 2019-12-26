package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.template.Template.{Context, Result}
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

object CalculateTemplate {
  val DEFAULT_ATTEMPT_LIMIT = 10

  def apply(assets: WorldAssets,
            area: Area,
            usedUniqueTemplates: Set[TemplateId],
            random: Random,
           )(template: Template): Result = {
    val context = Context(area, usedUniqueTemplates = usedUniqueTemplates)

    this.apply(assets, context, random, None)(template)
  }

  def apply(assets: WorldAssets,
            context: Context,
            random: Random,
            attemptLimit: Option[Int] = Some(DEFAULT_ATTEMPT_LIMIT)
           )(template: Template): Result = {

    var i = 0
    var result: Option[Result] = None

    while (result.isEmpty && attemptLimit.forall(_ > i)) {
      try {
        result = Some(template(assets, context, random))
      } catch {
        case _: CalculateFailedException =>
      }
      i = i + 1
    }

    if (result.isDefined) {
      result.get
    } else {
      throw new CalculateFailedException
    }
  }
}
