import React from 'react'
import { isPolymorphic, readItemId } from '../data/utils'
import { createFormFieldType, getFieldComponent } from './field/utils'
import TextInputRow from './form/TextInputRow.jsx'
import SideForm from './SideForm.jsx'

const ItemForm = ({
    item, itemRename, model, sideForm, data, models,
    deleteItem, renameItem, setItemRenameId, setItemValue, showItemReferences, showSideForm, hideSideForm
}) => {
    const submit = event => {
        event.preventDefault()

        return false
    }

    const id = readItemId(model, item)
    const path = model.dataPath.concat([id])
    const label = isPolymorphic(model) ? 'type' : undefined

    const fieldType = createFormFieldType(model)
    const common = {
        data,
        models,
        setValue: setItemValue,
        showSideForm,
        hideSideForm,
    }
    const Component = getFieldComponent(fieldType, model)

    const buttons = (
        <div className="btn-toolbar">
            <button
                type="button"
                onClick={() => showItemReferences(model, id)}
                className="btn btn-default">
                References
            </button>
            <button
                type="button"
                disabled={!itemRename.id || itemRename.id === id}
                onClick={() => renameItem(model, id, itemRename.id)}
                className={!itemRename.id || itemRename.id === id ? 'btn btn-default' : 'btn btn-primary'}>
                Rename
            </button>
            <button
                type="button"
                onClick={() => deleteItem(model, id)}
                className="btn btn-danger">
                DELETE
            </button>
        </div>
    )

    return  (
        <form onSubmit={submit} className="form-horizontal item-form">
            <div className="clearfix" style={{marginBottom: '1em'}}>
                <div className="pull-right">
                    {buttons}
                </div>
            </div>

            <TextInputRow
                label="id"
                error={itemRename.id === '' || itemRename.invalid}
                value={itemRename.id !== null ? itemRename.id : id}
                onChange={setItemRenameId} />
            <Component
                label={label}
                required={true}
                model={model}
                fieldType={fieldType}
                path={path}
                value={item}
                common={common} />
            <SideForm
                sideForm={sideForm}
                common={common} />
        </form>
    )
}

export default ItemForm
