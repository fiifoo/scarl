import React from 'react'
import { Button } from 'react-bootstrap'
import { Nav, NavItem } from 'react-bootstrap'
import { getTabRecipes, hasCraftingResources, tabs } from '../../game/crafting'
import Equipped from '../inventory/Equipped.jsx'
import ItemDetails from '../inventory/ItemDetails.jsx'

const NoRecipes = () => <i style={{marginLeft: 20}}>No recipes</i>

const getRecipe = (player, recipes, kinds, ui) => {
    const tab = tabs.get(ui.tab)

    return getTabRecipes(player, recipes, kinds)(tab).get(ui.row)
}

const Recipes = ({inventory, kinds, player, recipes, ui, craftItem, setRow}) => {
    const tab = tabs.get(ui.tab)
    const hasResources = hasCraftingResources(player, inventory)

    const renderRecipe = (recipe, index) => {
        const selected = index === ui.row
        const select = () => setRow(index)

        const kind = kinds.items.get(recipe.item)
        const disabled = ! hasResources(recipe)
        const className = [
            selected ? 'active' : null,
            disabled ? 'text-muted' : null,
        ].filter(x => !!x).join(' ')

        return (
            <tr
                key={recipe.id}
                className={className}
                onClick={select}>
                <td>
                    <Button
                        bsSize="xsmall"
                        onClick={() => craftItem(recipe.id)}
                        disabled={disabled}>
                        Craft
                    </Button>
                </td>
                <td className="full-width">{kind.name}</td>
            </tr>
        )
    }

    const tabRecipes = getTabRecipes(player, recipes, kinds)(tab)

    return (
        <table className="scarl-table">
            <tbody>
                {tabRecipes.isEmpty() ? <NoRecipes /> : tabRecipes.map(renderRecipe)}
            </tbody>
        </table>
    )
}

const Recipe = ({equipments, inventory, kinds, player, recipes, ui, craftItem}) => {
    const recipe = getRecipe(player, recipes, kinds, ui)

    if (! recipe) {
        return <div>&nbsp;</div>
    }

    const item = kinds.items.get(recipe.item)
    const hasComponents = recipe.cost.components <= player.creature.resources.components
    const owned = inventory.filter(item => item.kind === recipe.id).size
    const disabled = ! hasCraftingResources(player, inventory)(recipe)

    const renderRequiredItem = (id, index) => {
        const kind = kinds.items.get(id)
        const hasItem = inventory.find(item => item.kind === id) !== undefined

        return (
            <tr key={index} className={hasItem ? undefined : 'text-muted'}>
                <th className="text-right">Required item</th>
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
                        <th className="text-right">In inventory</th>
                        <td colSpan="2">{owned}</td>
                    </tr>
                    <tr className={hasComponents ? undefined : 'text-muted'}>
                        <th className="text-right">Required components</th>
                        <td colSpan="2">{recipe.cost.components}</td>
                    </tr>
                    {recipe.cost.items.map(renderRequiredItem)}
                    <tr>
                        <td colSpan="3">&nbsp;</td>
                    </tr>
                </tbody>
                <ItemDetails
                    equipments={equipments}
                    inventory={inventory}
                    item={item}
                    kinds={kinds} />
                <tbody>
                    <tr>
                        <td colSpan="3">&nbsp;</td>
                    </tr>
                    <tr>
                        <th></th>
                        <td colSpan="2">
                            <Button
                                onClick={() => craftItem(recipe.id)}
                                disabled={disabled}
                                style={{width: '100%'}}>
                                Craft
                            </Button>
                        </td>
                    </tr>
                    <tr>
                        <th></th>
                        <td colSpan="2">
                            <Button
                                onClick={() => craftItem(recipe.id, true)}
                                disabled={disabled}
                                style={{width: '100%'}}>
                                Craft and equip
                            </Button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    )
}

const RecycledItems = ({kinds, player, cancelRecycleItem}) => {
    const hasResources = kind => kind.recyclable && kind.recyclable <= player.creature.resources.components

    const renderItem = (item, index) => {
        const kind = kinds.items.get(item)

        return (
            <tr key={index}>
                <td className="full-width">{kind.name}</td>
                <td>
                    <Button
                        bsSize="xsmall"
                        onClick={() => cancelRecycleItem(item)}
                        disabled={! hasResources(kind)}>
                        Cancel
                    </Button>
                </td>
            </tr>
        )
    }

    return (
        <table className="scarl-table">
            <tbody>
                {player.recycledItems.map(renderItem)}
            </tbody>
        </table>
    )
}

const Crafting = props => {
    const { equipmentSet, equipments, inventory, kinds, ui, recycleItem, setEquipmentSet, setTab } = props

    return (
        <div>
            <div className="clearfix">
                <div className="pull-left">
                    <Nav bsStyle="pills" activeKey={ui.tab} onSelect={setTab}>
                        {tabs.map((tab, index) => (
                            <NavItem key={index} eventKey={index}>
                                {tab.label}
                            </NavItem>
                        ))}
                    </Nav>
                </div>
                <h4 className="pull-left" style={{marginLeft: 12, textDecoration: 'underline'}}>
                    Crafting components: {props.player.creature.resources.components}
                </h4>
            </div>

            <div className="scarl-panel">
                <h4>Recipes</h4>
                <Recipes {...props} />
            </div>
            <div className="scarl-panel">
                <Recipe {...props} />
            </div>
            <div className="scarl-panel">
                <Equipped
                    equipmentSet={equipmentSet}
                    equipments={equipments}
                    inventory={inventory}
                    kinds={kinds.items}
                    recycleItem={recycleItem}
                    setEquipmentSet={setEquipmentSet} />
                <br />
                <h4>Recycled items</h4>
                <RecycledItems {...props} />
            </div>
        </div>
    )
}

export default Crafting
