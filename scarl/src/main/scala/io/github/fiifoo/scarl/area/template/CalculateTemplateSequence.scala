package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.shape.{Shape, Rectangle => AreaRectangle}
import io.github.fiifoo.scarl.core.geometry.Shape.Rectangle
import io.github.fiifoo.scarl.core.geometry.{Distance, Location, Rotation}

import scala.annotation.tailrec

object CalculateTemplateSequence {

  def apply(templates: List[Template.Result]): (Map[Location, Template.Result], Shape.Result) = {
    if (templates.isEmpty || templates.head.entrances.isEmpty) {
      throw new CalculateFailedException
    }

    val initial = (Map(Location(0, 0) -> templates.head), templates.head.entrances.head)

    val (results, _) = (templates.tail foldLeft initial) ((x, template) => {
      val (results, target) = x

      val entrance = template.entrances.headOption match {
        case Some(x) => x
        case None => throw new CalculateFailedException
      }

      val calculate =
        (this.rotate(results, target) _).tupled andThen
          (this.position(results, target) _).tupled andThen
          (this.next(results) _).tupled

      calculate(template, entrance)
    })

    finalize(results)
  }

  private def rotate(results: Map[Location, Template.Result], target: Location)
                    (template: Template.Result, entrance: Location): (Template.Result, Location, Location) = {
    val choices = (0 until 4) map (x => {
      val rotation = Rotation(x, template.shape.outerWidth, template.shape.outerHeight).reverse

      (template.rotate(rotation), target.sub(rotation(entrance)), rotation(entrance))
    })

    val weight = ((template: Template.Result, location: Location, _: Location) => {
      results
        .map(x => {
          val (existingLocation, existing) = x

          val a = Rectangle(location, template.shape.outerWidth, template.shape.outerHeight)
          val b = Rectangle(existingLocation, existing.shape.outerWidth, existing.shape.outerHeight)

          a.intersectionSize(b)
        })
        .sum
    }).tupled

    val (selected, _) = (choices.tail foldLeft(choices.head, weight(choices.head))) ((x, choice) => {
      val (selected, selectedWeight) = x
      val choiceWeight = weight(choice)

      if (choiceWeight < selectedWeight) {
        (choice, choiceWeight)
      } else {
        (selected, selectedWeight)
      }
    })

    selected
  }

  private def position(results: Map[Location, Template.Result], target: Location)
                      (template: Template.Result, location: Location, entrance: Location): (Template.Result, Location, Location) = {
    @tailrec
    def find(step: Location, location: Location): Location = {
      if (results.exists(x => {
        val (existingLocation, existing) = x

        val a = Rectangle(Location(location.x - 1, location.y - 1), template.shape.outerWidth + 2, template.shape.outerHeight + 2)
        val b = Rectangle(existingLocation, existing.shape.outerWidth, existing.shape.outerHeight)

        a.intersects(b)
      })) {
        find(step, location.add(step))
      } else {
        location
      }
    }

    val candidates = List(Location(1, 0), Location(0, 1), Location(-1, 0), Location(0, -1)) map (find(_, location))

    val result = (candidates.tail foldLeft candidates.head) ((result, candidate) => {
      if (Distance(location, candidate) < Distance(location, result)) {
        candidate
      } else {
        result
      }
    })

    (template, result, entrance)
  }

  private def next(results: Map[Location, Template.Result])
                  (template: Template.Result, location: Location, entrance: Location): (Map[Location, Template.Result], Location) = {
    val nextEntrance = (template.entrances - entrance).headOption.getOrElse(entrance)

    (results + (location -> template), location.add(nextEntrance))
  }

  private def finalize(results: Map[Location, Template.Result]): (Map[Location, Template.Result], Shape.Result) = {
    val (x1, x2, y1, y2) = (results foldLeft(0, 0, 0, 0)) ((carry, item) => {
      val (x1, x2, y1, y2) = carry
      val (location, template) = item

      (
        math.min(x1, location.x),
        math.max(x2, location.x + template.shape.outerWidth),
        math.min(y1, location.y),
        math.max(y2, location.y + template.shape.outerHeight),
      )
    })

    val offset = Location(-x1 + 2, -y1 + 2)
    val width = x2 - x1 + 4
    val height = y2 - y1 + 4

    (
      results map (x => {
        val (location, template) = x

        location.add(offset) -> template
      }),
      AreaRectangle(width, height).apply()
    )
  }
}
