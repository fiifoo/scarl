import React from 'react'
import SelectRow from '../form/SelectRow.jsx'

const ItemAdd = ({data, models, ui, setItems}) => {
    const model = models.main.get(ui.model)
    const items = data.getIn(model.dataPath)
    const addItem = item => setItems(ui.items.push(item))

    const filterItem = (_, id) => ! ui.items.contains(id)
    const createChoice = (_, id) => ({
        value: id,
        label: id,
    })

    const choices = items.filter(filterItem).map(createChoice).toArray()

    return (
        <SelectRow
            label="Add item"
            input={{closeOnSelect: false, onSelectResetsInput: false}}
            choices={choices}
            value={null}
            onChange={addItem} />
    )
}

export default ItemAdd
