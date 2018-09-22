package models.json

import io.github.fiifoo.scarl.core.item.Recipe
import io.github.fiifoo.scarl.core.item.Recipe.{Cost, RecipeId}
import play.api.libs.json._

object JsonRecipe {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat

  lazy private implicit val costFormat = Json.format[Cost]

  lazy implicit val recipeIdFormat: Format[RecipeId] = stringIdFormat(_.value, RecipeId)
  lazy implicit val recipeFormat: Format[Recipe] = Json.format[Recipe]

  lazy val recipeMapReads: Reads[Map[RecipeId, Recipe]] = mapReads
}
