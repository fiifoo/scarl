package io.github.fiifoo.scarl.area

import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection.{FixedItem, FixedWall}
import io.github.fiifoo.scarl.area.template.FixedContent
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.{ItemKindId, WallKindId}

object Utils {

  object TemplateMock {
    private val wall = WallKindId("")
    private val item = ItemKindId("")

    def apply(sketch: String): TemplateMock = {
      val lines = sketch.lines.toList

      val template = TemplateMock(
        width = lines.head.toCharArray.length,
        height = lines.length
      )

      (lines.zipWithIndex foldLeft template) (readLine)
    }

    private def readLine(template: TemplateMock, row: (String, Int)): TemplateMock = {
      val (line, y) = row

      (line.toCharArray.zipWithIndex foldLeft template) (readChar(y))
    }

    private def readChar(y: Int)(template: TemplateMock, cell: (Char, Int)): TemplateMock = {
      val (char, x) = cell
      val location = Location(x + 1, y + 1)

      char match {
        case '#' => template.copy(
          locations = template.locations + location,
          content = template.content.copy(
            walls = template.content.walls + (location -> FixedWall(wall))
          )
        )
        case '/' => template.copy(
          locations = template.locations + location,
          content = template.content.copy(
            items = template.content.items + (location -> List(FixedItem(item)))
          )
        )
        case '.' => template.copy(
          locations = template.locations + location
        )
        case _ => template
      }
    }
  }

  case class TemplateMock(width: Int,
                          height: Int,
                          locations: Set[Location] = Set(),
                          content: FixedContent = FixedContent()
                         ) {

    def sketch: String = {
      ((0 until this.height) foldLeft "") (writeLine)
    }

    def shape: Shape.Result = {
      Shape.Result(
        outerWidth = this.width + 2,
        outerHeight = this.height + 2,
        innerWidth = this.width + 2,
        innerHeight = this.height + 2,
        border = Set(),
        contained = this.locations,
        entranceCandidates = Set()
      )
    }

    private def writeLine(sketch: String, y: Int): String = {
      ((0 until this.width) foldLeft sketch) (writeChar(y)) + "\n"
    }

    private def writeChar(y: Int)(sketch: String, x: Int): String = {
      val location = Location(x + 1, y + 1)

      val char = if (this.locations contains location) {
        if (this.content.walls.isDefinedAt(location)) {
          '#'
        } else if (this.content.items.isDefinedAt(location)) {
          '/'
        } else {
          '.'
        }
      } else {
        ' '
      }

      sketch + char
    }
  }

}
