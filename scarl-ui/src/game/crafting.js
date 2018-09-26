import { tabs as inventoryTabs } from './inventory'

export const tabs = inventoryTabs.filter(x => x.key !== 'Usable' && x.key !== 'Other')

export const getTabRecipes = (player, recipes, kinds) => tab => {
    const tabRecipes = player.recipes.map(x => recipes.get(x)).filter(recipe => {
        const item = kinds.items.get(recipe.item)

        return item[tab.prop] !== undefined
    })

    return tabRecipes.sortBy(x => kinds.items.get(x.item).name)
}

export const hasCraftingResources = (player, inventory) => recipe => {
    const cost = recipe.cost

    if (cost.components > player.creature.resources.components) {
        return false
    }

    return cost.items.find(kind => (
        inventory.find(item => item.kind === kind) === undefined
    )) === undefined
}
