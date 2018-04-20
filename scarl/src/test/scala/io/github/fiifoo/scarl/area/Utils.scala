package io.github.fiifoo.scarl.area

import io.github.fiifoo.scarl.area.template.FixedContent
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.WallKindId

object Utils {

  object TemplateMock {
    private val wall = WallKindId("")

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
            walls = template.content.walls + (location -> wall)
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

    private def writeLine(sketch: String, y: Int): String = {
      ((0 until this.width) foldLeft sketch) (writeChar(y)) + "\n"
    }

    private def writeChar(y: Int)(sketch: String, x: Int): String = {
      val location = Location(x + 1, y + 1)

      val char = if (this.locations contains location) {
        if (this.content.walls isDefinedAt location) {
          '#'
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
