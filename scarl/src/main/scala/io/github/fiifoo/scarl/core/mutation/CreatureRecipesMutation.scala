package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId

case class CreatureRecipesMutation(creature: CreatureId, recipes: Set[RecipeId]) extends Mutation {

  def apply(s: State): State = {
    s.copy(recipes = s.recipes + (creature -> recipes))
  }
}
