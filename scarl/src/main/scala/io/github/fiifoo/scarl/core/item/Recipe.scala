package io.github.fiifoo.scarl.core.item

import io.github.fiifoo.scarl.core.item.Recipe.{Cost, RecipeId}
import io.github.fiifoo.scarl.core.kind.ItemKindId

object Recipe {

  case class RecipeId(value: String)

  case class Cost(components: Int, items: Set[ItemKindId])

}

case class Recipe(id: RecipeId, cost: Cost, item: ItemKindId)
