import { Button } from 'react-bootstrap'
import React from 'react'
import { hasCraftingResources } from '../../game/crafting'
import ItemDetails from '../inventory/ItemDetails.jsx'

const getRecipe = (player, recipes, ui) => {
    const row = ui.row
    const id = player.recipes.toArray()[row]

    if (id) {
        return recipes.get(id)
    } else {
        return undefined
    }
}

const Recipes = ({inventory, kinds, player, recipes, ui, craftItem, setRow}) => {
    const hasResources = hasCraftingResources(player, inventory)

    const renderRecipe = (recipe, index) => {
        const selected = index === ui.row
        const select = () => setRow(index)

        const kind = kinds.items.get(recipe.item)

        return (
            <tr
                key={recipe.id}
                className={selected ? 'active' : null}
                onClick={select}>
                <td>
                    <Button
                        bsSize="xsmall"
                        onClick={() => craftItem(recipe.id)}
                        disabled={! hasResources(recipe)}>
                        Craft
                    </Button>
                </td>
                <td className="full-width">{kind.name}</td>
            </tr>
        )
    }

    return (
        <table className="scarl-table">
            <tbody>
                {player.recipes.toArray().map(x => recipes.get(x)).map(renderRecipe)}
            </tbody>
        </table>
    )
}

const Recipe = ({equipments, inventory, kinds, player, recipes, ui, craftItem}) => {
    const recipe = getRecipe(player, recipes, ui)

    if (! recipe) {
        return undefined
    }

    const item = kinds.items.get(recipe.item)
    const hasComponents = recipe.cost.components <= player.creature.resources.components
    const owned = inventory.filter(item => item.kind === recipe.id).size

    const renderRequiredItem = (id, index) => {
        const kind = kinds.items.get(id)
        const hasItem = inventory.find(item => item.kind === id) !== undefined

        return (
            <tr key={index} className={hasItem ? undefined : 'text-muted'}>
                <th>Required item</th>
                <td colSpan="2">{kind.name}</td>
            </tr>
        )
    }

    return (
        <div>
            <h4>{item.name}</h4>
            <table className="scarl-table">
                <tbody>
                    <tr>
                        <th>In inventory</th>
                        <td colSpan="2">{owned}</td>
                    </tr>
                    <tr className={hasComponents ? undefined : 'text-muted'}>
                        <th>Required components</th>
                        <td colSpan="2">{recipe.cost.components}</td>
                    </tr>
                    {recipe.cost.items.map(renderRequiredItem)}
                </tbody>
                <ItemDetails
                    equipments={equipments}
                    inventory={inventory}
                    item={item}
                    kinds={kinds} />
                <tbody>
                    <tr>
                        <th></th>
                        <td colSpan="2">
                            <Button
                                onClick={() => craftItem(recipe.id)}
                                disabled={! hasCraftingResources(player, inventory)(recipe)}>
                                Craft
                            </Button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    )
}

const Crafting = props => {
    return (
        <div>
            <h4>Crafting components: {props.player.creature.resources.components}</h4>
            <div className="scarl-panel">
                <h4>Recipes</h4>
                <Recipes {...props} />
            </div>
            <div className="scarl-panel">
                <Recipe {...props} />
            </div>
        </div>
    )
}

export default Crafting
