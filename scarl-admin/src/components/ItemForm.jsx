import React from 'react'
import { readItemId } from '../data/utils'
import { createFormFieldType, getFieldComponent } from './field/utils'
import TextInputRow from './form/TextInputRow.jsx'
import SideForm from './SideForm.jsx'

const ItemForm = ({
    item, itemRename, model, sideForm, data, models, tags,
    deleteItem, renameItem, setItemRenameId, setItemValue, addTag, showItem, showItemReferences, showSideForm, hideSideForm
}) => {
    const id = readItemId(model, item)
    const path = model.dataPath.concat([id])

    const fieldType = createFormFieldType(model)
    const common = {
        data,
        models,
        tags,
        setValue: setItemValue,
        addTag,
        showItem,
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
        <div className="form-horizontal item-form">
            <SideForm
                sideForm={sideForm}
                common={common} />
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
                required={true}
                model={model}
                fieldType={fieldType}
                path={path}
                value={item}
                common={common} />
        </div>
    )
}

export default ItemForm
