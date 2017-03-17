package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.core.{Location, Rng}

import scala.util.Random

object CalculateTemplateLocations {

  type Results = Map[Location, Template.Result]
  type Area = (Location, Location)
  type Areas = Set[Area]

  def apply(templates: List[Template.Result],
            shape: Shape.Result,
            random: Random
           ): Results = {

    val area = (Location(1, 1), Location(shape.innerWidth - 1, shape.innerHeight - 1))
    val sorted = templates.sortWith((a, b) => {
      a.shape.outerWidth + a.shape.outerHeight > b.shape.outerWidth + b.shape.outerHeight
    })
    val foldTemplates = sorted.foldLeft[(Results, Areas)]((Map(), Set(area))) _

    val (results, _) = foldTemplates((carry, template) => {
      val (results, areas) = carry

      val width = template.shape.outerWidth
      val height = template.shape.outerHeight
      val (location, nextAreas) = seek(width, height, areas, random)

      (results + (location -> template), nextAreas)
    })

    results
  }

  private def seek(width: Int, height: Int, areas: Areas, random: Random): (Location, Areas) = {
    val validAreas = areas filter (area => {
      val (a, b) = area

      b.x - a.x >= width + 2 && b.y - a.y >= height + 2
    })

    if (validAreas.isEmpty) {
      throw new CalculateFailedException
    }

    val area = Rng.nextChoice(random, validAreas)
    val location = getLocation(width, height, area, random)
    val used = (location, Location(location.x + width, location.y + height))
    val nextAreas = calculateNextAreas(areas, area, used)

    (location, nextAreas)
  }

  private def calculateNextAreas(areas: Areas, current: Area, used: Area): Areas = {
    val ca = current._1
    val cb = current._2
    val ua = used._1
    val ub = used._2

    val next = Set(
      (Location(ua.x, ca.y), Location(cb.x, ua.y)),
      (Location(ub.x, ua.y), Location(cb.x, cb.y)),
      (Location(ca.x, ub.y), Location(ub.x, cb.y)),
      (Location(ca.x, ca.y), Location(ua.x, ub.y))
    )

    areas - current ++ next
  }

  private def getLocation(width: Int, height: Int, area: Area, random: Random): Location = {
    val (a, b) = area

    Location(
      a.x + random.nextInt(b.x - a.x - width - 1) + 1,
      a.y + random.nextInt(b.y - a.y - height - 1) + 1
    )
  }
}
