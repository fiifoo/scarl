package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.template.Template.Result

import scala.util.Random

object CalculateTemplate {

  def apply(template: Template,
            templates: Map[TemplateId, Template],
            random: Random,
            attemptLimit: Option[Int] = None
           ): Result = {

    var i = 0
    var result: Option[Result] = None

    while (result.isEmpty && attemptLimit.forall(_ > i)) {
      try {
        result = Some(template(templates, random))
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
