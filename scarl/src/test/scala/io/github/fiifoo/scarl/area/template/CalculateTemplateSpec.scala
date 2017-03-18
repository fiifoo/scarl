package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.shape.Rectangle
import io.github.fiifoo.scarl.core.Location
import org.scalatest._

import scala.util.Random

class CalculateTemplateSpec extends FlatSpec with Matchers {

  "CalculateTemplate" should "calculate template tree" in {
    val t1 = RandomizedTemplate(
      id = TemplateId("main"),
      shape = Rectangle(80, 25, 0),
      templates = List(
        (TemplateId("fixed-sub"), 1, 1),
        (TemplateId("random-sub-1"), 1, 1)
      )
    )
    val t2 = FixedTemplate(
      id = TemplateId("fixed-sub"),
      shape = Rectangle(20, 10, 0),
      templates = Map(
        Location(1, 1) -> TemplateId("random-sub-1"),
        Location(10, 1) -> TemplateId("random-sub-2")
      )
    )
    val t3 = RandomizedTemplate(
      id = TemplateId("random-sub-1"),
      shape = Rectangle(3, 3, 0)
    )
    val t4 = RandomizedTemplate(
      id = TemplateId("random-sub-2"),
      shape = Rectangle(3, 3, 0.5)
    )

    val t = Map(t1.id -> t1, t2.id -> t2, t3.id -> t3, t4.id -> t4)

    CalculateTemplate(t1, t, new Random(1), Some(1))
  }
}