import { List } from 'immutable'
import React from 'react'
import ListField from '../field/ListField.jsx'
import PolymorphicFormField from '../field/PolymorphicFormField.jsx'
import SelectRow from '../form/SelectRow.jsx'

const selectionModels = {
    creature: 'ContentSelection.CreatureSelection',
    entrance: 'ContentSelection.DoorSelection',
    items: 'ContentSelection.ItemSelection',
    terrain: 'ContentSelection.TerrainSelection',
    wall: 'ContentSelection.WallSelection',
    widget: 'ContentSelection.WidgetSelection',
}

const SelectionField = (label, multi = false) => ({property, value, setValue, common}) => {
    const {models} = common

    const model = models.sub.get(selectionModels[property])

    const setFormValue = (path, formValue) => {
        if (path.length === 0) {
            setValue(formValue)
        } else {
            setValue(value.setIn(path, formValue))
        }
    }

    if (multi) {
        const formFieldType = {
            type: 'FormField',
            data: {
                model: model.id,
                required: true,
            }
        }

        const listFieldType = {
            type: 'ListField',
            data: {
                value: formFieldType,
                required: false,
            }
        }

        return (
            <ListField
                label={label}
                fieldType={listFieldType}
                path={[]}
                common={{
                    ...common,
                    setValue: setFormValue
                }}
                value={value} />
        )
    } else {
        const fieldType = {
            type: 'FormField',
            data: {
                model: model.id,
                required: false,
            }
        }

        return (
            <PolymorphicFormField
                label={label}
                model={model}
                fieldType={fieldType}
                path={[]}
                common={{
                    ...common,
                    setValue: setFormValue
                }}
                value={value} />
        )
    }
}

const getMachineryChoices = machinery => machinery.map((machinery, key) => ({
    value: key,
    label: `${machinery.getIn(['mechanism', 'type'])} (${key})`,
})).toArray()

const MachineryField = label => ({value, machinery, setValue}) => (
    <SelectRow
        label={label}
        choices={getMachineryChoices(machinery)}
        value={value ? value.toArray() : []}
        multi={true}
        onChange={value => setValue(List(value))} />
)

const components = {
    machineryControls: MachineryField('Machinery controls'),
    machineryTargets: MachineryField('Machinery targets'),
    creature: SelectionField('Creature'),
    entrance: SelectionField('Entrance'),
    items: SelectionField('Items', true),
    terrain: SelectionField('Terrain'),
    wall: SelectionField('Wall'),
    widget: SelectionField('Widget'),
}

const EditorLocationField = props => {
    const {property} = props
    const Component = components[property]

    return <Component {...props} />
}

export default EditorLocationField
