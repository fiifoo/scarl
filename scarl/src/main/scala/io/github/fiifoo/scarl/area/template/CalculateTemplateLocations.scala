package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.math.Rng

import scala.util.Random

object CalculateTemplateLocations {

  type Results = Map[Location, Template.Result]
  type Area = (Location, Location)
  type Areas = Set[Area]

  def apply(required: List[Template.Result],
            optional: List[Template.Result],
            shape: Shape.Result,
            random: Random
           ): Results = {

    val calculateRequired = calculateTemplates(required.sortWith(sort), random, required = true) _
    val calculateOptional = calculateTemplates(optional.sortWith(sort), random, required = false) _

    val area = (Location(1, 1), Location(shape.innerWidth - 1, shape.innerHeight - 1))
    val (results, _) = calculateOptional(calculateRequired((Map(), Set(area))))

    results
  }

  private def sort(a: Template.Result, b: Template.Result): Boolean = {
    a.shape.outerWidth + a.shape.outerHeight > b.shape.outerWidth + b.shape.outerHeight
  }

  private def calculateTemplates(templates: List[Template.Result], random: Random, required: Boolean)
                                (carry: (Results, Areas)): (Results, Areas) = {
    (templates foldLeft carry) (calculateTemplate(random, required))
  }

  private def calculateTemplate(random: Random, required: Boolean)
                               (carry: (Results, Areas), template: Template.Result): (Results, Areas) = {
    val (results, areas) = carry

    val width = template.shape.outerWidth
    val height = template.shape.outerHeight

    seek(width, height, areas, random) map (result => {
      val (location, nextAreas) = result

      (results + (location -> template), nextAreas)
    }) getOrElse {
      if (required) {
        throw new CalculateFailedException
      }

      (results, areas)
    }
  }

  private def seek(width: Int, height: Int, areas: Areas, random: Random): Option[(Location, Areas)] = {
    val validAreas = areas filter (area => {
      val (a, b) = area

      b.x - a.x >= width + 2 && b.y - a.y >= height + 2
    })

    if (validAreas.isEmpty) {
      None
    } else {
      val area = Rng.nextChoice(random, validAreas)
      val location = getLocation(width, height, area, random)
      val used = (location, Location(location.x + width, location.y + height))
      val nextAreas = calculateNextAreas(areas, area, used)

      Some((location, nextAreas))
    }
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
