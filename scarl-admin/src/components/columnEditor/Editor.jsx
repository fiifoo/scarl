import { List } from 'immutable'
import React from 'react'
import { getFieldComponent, getFieldModel } from '../field/utils'

const RemoveButton = props => (
    <button
        type="button"
        className="btn btn-xs btn-link"
        {...props}>
        &#10060;
    </button>
)

const Editor = ({
    data, models, tags, ui,
    setItems, setProperties, setItemValue, addTag,
}) => {

    const removeItem = item => {
        const index = ui.items.indexOf(item)

        setItems(ui.items.remove(index))
    }
    const removeProperty = property => {
        const index = ui.properties.indexOf(property)

        setProperties(ui.properties.remove(index))
    }

    const dataPath = models.main.get(ui.model).dataPath

    const common = {
        horizontal: false,
        data,
        models,
        tags,
        setValue: setItemValue,
        addTag,
    }

    const renderHeader = () => (
        <tr>
            <td><RemoveButton onClick={() => setItems(List())}/></td>
            {ui.properties.map(renderHeaderProperty)}
        </tr>
    )

    const renderHeaderProperty = property => (
        <th key={property.hashCode()}>
            <RemoveButton onClick={() => removeProperty(property)} />
            {property.path.map(p => (
                <div key={p}>{p}</div>
            ))}
        </th>
    )

    const renderItem = item => (
        <tr key={item}>
            <th>
                <RemoveButton onClick={() => removeItem(item)} />
                {item}
            </th>
            {ui.properties.map(renderItemProperty(item))}
        </tr>
    )

    const renderItemProperty = item => property => {
        const parentPath = dataPath.concat([item]).concat(property.path.butLast().toArray())
        const parentValue = data.getIn(parentPath)

        if (! parentValue) {
            return (
                <td key={property.hashCode()} />
            )
        }

        const name = property.path.last()
        const path = parentPath.concat([name])
        const fieldType = property.fieldType

        const value = parentValue.get(name)
        const model = getFieldModel(fieldType, models)
        const Component = getFieldComponent(fieldType, model)

        return (
            <td key={property.hashCode()}>
                <Component
                    name={name}
                    required={fieldType.data.required}
                    model={model}
                    fieldType={fieldType}
                    path={path}
                    value={value}
                    common={common} />
            </td>
        )
    }

    return (
        <table>
            <thead>
                {renderHeader()}
            </thead>
            <tbody>
                {ui.items.sort().map(renderItem)}
            </tbody>
        </table>
    )
}

export default Editor
