package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

object TestTemplates {

  val iterations = 10

  def apply(assets: WorldAssets, random: Random = new Random(1)): Map[AreaId, Map[TemplateId, Int]] = {
    assets.areas mapValues testArea(assets, random)
  }

  private def testArea(assets: WorldAssets, random: Random)(area: Area): Map[TemplateId, Int] = {
    val template = assets.templates(area.template)
    val fromArea = extractTemplates(assets)(template)
    val fromTheme = template match {
      case _: FixedTemplate => Set()
      case _: RandomizedTemplate =>
        assets.catalogues.templates(assets.themes(area.theme).templates)
          .apply(assets.catalogues.templates)
          .flatMap(_._2 map (_.value))
          .map(assets.templates)
    }

    val templates = ((fromArea ++ fromTheme) map (t => t.id -> t)).toMap

    templates mapValues testTemplate(assets, area, random)
  }

  private def testTemplate(assets: WorldAssets, area: Area, random: Random)(template: Template): Int = {
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

  private def extractTemplates(assets: WorldAssets)(template: Template): Set[Template] = {
    val subs = (template match {
      case template: FixedTemplate => template.templates.values
      case template: RandomizedTemplate => template.templates flatMap (_.selection match {
        case selection: ContentSelection.FixedTemplate => Some(selection.template)
        case _ => None
      })
    }).toSet

    (subs map assets.templates flatMap extractTemplates(assets)) + template
  }
}
