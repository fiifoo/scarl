import React from 'react'
import Editor from './Editor.jsx'
import ItemAdd from './ItemAdd.jsx'
import ModelSelect from './ModelSelect.jsx'
import PropertyAdd from './PropertyAdd.jsx'

const ColumnEditor = ({
    data, models, tags, ui,
    setModel, setItems, setProperties, setItemValue, addTag,
}) =>  (
    <div className="column-editor">
        <div className="form-horizontal left-content">
            <ModelSelect models={models} ui={ui} setModel={setModel} />

            {ui.model && (
                <PropertyAdd models={models} ui={ui} setProperties={setProperties} />
            )}
            {ui.model && (
                <ItemAdd data={data} models={models} ui={ui} setItems={setItems} />
            )}
        </div>
        {ui.model && (
            <Editor
                data={data}
                models={models}
                tags={tags}
                ui={ui}
                setItems={setItems}
                setProperties={setProperties}
                setItemValue={setItemValue}
                addTag={addTag} />
        )}
    </div>
)

export default ColumnEditor
