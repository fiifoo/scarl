import { List } from 'immutable'
import React from 'react'
import {SortableContainer, SortableElement, SortableHandle} from 'react-sortable-hoc'
import { getNewValue, isPolymorphic } from '../../data/utils'
import FormRow from '../form/FormRow.jsx'
import PolymorphicObjectField from './PolymorphicObjectField.jsx'
import { getFieldComponent, getFieldModel } from './utils'

const ListField = ({value, ...props}) => {
    const {fieldType, path, common, filterValue = undefined} = props
    const {models, setValue} = common

    if (! value) {
        value = List()
    }

    const valueFieldType = fieldType.data.value
    const valueModel = getFieldModel(valueFieldType, models)

    if (valueFieldType.type === 'FormField' && isPolymorphic(valueModel) && valueModel.objectPolymorphism) {
        return (
            <SelectComponent Component={PolymorphicObjectField} value={value} {...props} />
        )
    }

    const onSortEnd = ({oldIndex, newIndex}) => {
        const item = value.get(oldIndex)
        const newValue = value.delete(oldIndex).insert(newIndex, item)

        setValue(path, newValue)
    }

    return (
        <ListComponent
            value={value}
            filterValue={filterValue}
            onSortEnd={onSortEnd}
            useDragHandle={true}
            useWindowAsScrollContainer={true}
            {...props} />
    )
}

const SelectComponent = ({Component, label, fieldType, path, value, common}) => {
    const {models} = common

    const valueFieldType = fieldType.data.value
    const valueModel = getFieldModel(valueFieldType, models)

    return (
        <Component
            label={label}
            required={valueFieldType.data.required}
            model={valueModel}
            fieldType={valueFieldType}
            path={path}
            value={value}
            multi={true}
            common={common} />
    )
}

const ListComponent = SortableContainer(({label, fieldType, path, value, common, filterValue}) => {
    const {horizontal, models, setValue} = common

    const valueFieldType = fieldType.data.value
    const valueModel = getFieldModel(valueFieldType, models)
    const ValueComponent = getFieldComponent(valueFieldType, valueModel)

    const add = () => setValue(path, value.push(getNewValue(valueFieldType, models)))

    const renderValueField = (subValue, index) => (
        filterValue !== undefined && ! filterValue(subValue) ? null : (
            <Item
                key={index}
                index={index}
                valueIndex={index}
                subValue={subValue}
                valueFieldType={valueFieldType}
                valueModel={valueModel}
                ValueComponent={ValueComponent}
                path={path}
                value={value}
                common={common} />
        )
    )

    return (
        <FormRow label={label} horizontal={horizontal}>
            {value.map(renderValueField)}
            <button
                type="button"
                className="btn btn-success"
                onClick={add}>
                Add
            </button>
        </FormRow>
    )
})

const Item = SortableElement(props => {
    const {valueIndex, subValue, valueFieldType, valueModel, ValueComponent} = props
    const {path, value, common} = props
    const {setValue} = common

    const valuePath = path.concat([valueIndex])

    return (
        <div className="item-form-sortable-row">
            <div className="row-extras">
                <button
                    type="button"
                    className="btn btn-danger"
                    onClick={() => setValue(path, value.remove(valueIndex))}>
                    Remove
                </button>
                <DragHandle />
            </div>
            <ValueComponent
                required={valueFieldType.data.required}
                model={valueModel}
                fieldType={valueFieldType}
                path={valuePath}
                value={subValue}
                common={common} />
        </div>
    )
})

const DragHandle = SortableHandle(() => <span className="noselect drag-handle"></span>)

export default ListField
