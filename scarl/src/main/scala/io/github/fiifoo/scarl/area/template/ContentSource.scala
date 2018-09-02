package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Distribution

sealed trait ContentSource[T] {
  val selection: ContentSelection[T]
  val distribution: Distribution
}

object ContentSource {

  case class CreatureSource(selection: ContentSelection.CreatureSelection,
                            distribution: Distribution
                           ) extends ContentSource[CreatureKindId]

  case class ItemSource(selection: ContentSelection.ItemSelection,
                        distribution: Distribution
                       ) extends ContentSource[ItemKindId]

  case class TemplateSource(selection: ContentSelection.TemplateSelection,
                            distribution: Distribution,
                            required: Int = 0,
                           ) extends ContentSource[TemplateId]

  case class WidgetSource(selection: ContentSelection.WidgetSelection,
                          distribution: Distribution
                         ) extends ContentSource[WidgetKindId]

}
