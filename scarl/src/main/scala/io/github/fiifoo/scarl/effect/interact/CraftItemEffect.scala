package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult, RemoveEntityEffect}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.getContainerItems
import io.github.fiifoo.scarl.core.item.Recipe.{Cost, RecipeId}
import io.github.fiifoo.scarl.core.kind.Kind.Options
import io.github.fiifoo.scarl.core.mutation.{CreatureComponentsMutation, Mutation}
import io.github.fiifoo.scarl.effect.creature.ShortageEffect

case class CraftItemEffect(craftsman: CreatureId,
                           recipe: RecipeId,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    val recipe = s.assets.recipes(this.recipe)

    if (!this.hasResources(s: State, recipe.cost)) {
      EffectResult(ShortageEffect(this.craftsman, components = true, parent = Some(this)))
    } else {
      val (costEffects, costMutations) = this.getCostResults(s, recipe.cost)
      val craftedEffect = ItemCraftedEffect(this.craftsman, recipe.item, Some(this))
      val itemMutations = recipe.item(s).apply(s, s.idSeq, this.craftsman, Options()).mutations

      EffectResult(costMutations ::: itemMutations, costEffects ::: List(craftedEffect))
    }
  }

  private def hasResources(s: State, cost: Cost): Boolean = {
    if (cost.components > this.craftsman(s).resources.components) {
      return false
    }

    if (cost.items.nonEmpty) {
      val inventory = getContainerItems(s)(this.craftsman) map (_ (s).kind)

      !(cost.items exists (!inventory.contains(_)))
    } else {
      true
    }
  }

  private def getCostResults(s: State, cost: Cost): (List[Effect], List[Mutation]) = {
    val componentsMutation = CreatureComponentsMutation(
      this.craftsman,
      this.craftsman(s).resources.components - cost.components
    )

    val disassembleEffects = if (cost.items.nonEmpty) {
      val location = this.craftsman(s).location
      val equipped = s.equipments.getOrElse(this.craftsman, Map()).values.toSet
      val inventory = getContainerItems(s)(this.craftsman) map (_.apply(s))

      val (effects, _) = (cost.items foldLeft(List[Effect](), inventory)) ((carry, kind) => {
        val (effects, inventory) = carry

        inventory find (_.kind == kind) map (item => {
          val remove = RemoveEntityEffect(item.id, parent = Some(this))

          if (equipped.contains(item.id)) {
            val unequip = UnequipItemEffect(this.craftsman, item.id, location, Some(this))

            (unequip :: remove :: effects, inventory - item)
          } else {
            (remove :: effects, inventory - item)
          }
        }) getOrElse {
          carry
        }
      })

      effects
    } else {
      List()
    }

    (disassembleEffects, List(componentsMutation))
  }
}
