package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.math.Rng

import scala.util.Random

object CalculateTemplateLocations {

  private val requiredMultiplier = 1.5

  type Source = (Template.Result, Boolean)
  type Results = Map[Location, Template.Result]
  type Area = (Location, Location)
  type Areas = Set[Area]

  def apply(templates: List[Source],
            shape: Shape.Result,
            random: Random
           ): Results = {
    val area = (Location(1, 1), Location(shape.innerWidth - 1, shape.innerHeight - 1))
    val initial: (Results, Areas) = (Map(), Set(area))

    val (results, _) = (templates sortWith sort foldLeft initial) (calculateTemplate(random))

    results
  }

  private def sort(a: Source, b: Source): Boolean = {
    weight(a) > weight(b)
  }

  private def weight(source: Source): Int = {
    val (template, required) = source

    val weight = template.shape.outerWidth + template.shape.outerHeight

    if (required) {
      (weight * requiredMultiplier).toInt
    } else {
      weight
    }
  }

  private def calculateTemplate(random: Random)
                               (carry: (Results, Areas), source: Source): (Results, Areas) = {
    val (results, areas) = carry
    val (template, required) = source

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
