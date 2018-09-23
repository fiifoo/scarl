export const hasCraftingResources = (player, inventory) => recipe => {
    const cost = recipe.cost

    if (cost.components > player.creature.resources.components) {
        return false
    }

    return cost.items.find(kind => (
        inventory.find(item => item.kind === kind) === undefined
    )) === undefined
}
