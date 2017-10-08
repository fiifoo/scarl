import React from 'react'
import LocationContent from '../../data/area/LocationContent'
import EditorBrush from './EditorBrush.jsx'
import EditorLocation from './EditorLocation.jsx'
import EditorView from './EditorView.jsx'

import './Editor.css'

const Editor = ({path, value, common, editor, setEditorBrush, setEditorLocation}) => {
    const {data, setValue} = common

    const contents = LocationContent.read(value, data)
    const setLocationContent = location => content => setValue(path, LocationContent.write(value, location, content))

    const editorView = (
        <div>
            <h4 className="text-center">Preview</h4>
            <EditorView
                common={common}
                contents={contents}
                editor={editor}
                shape={value.get('shape')}
                setEditorLocation={setEditorLocation}
                setLocationContent={setLocationContent} />
        </div>

    )

    const editorBrush = (
        <div>
            {editor.brush.property && <b className="pull-right text-danger">ACTIVE</b>}
            <h4 className="text-center">Brush</h4>
            <EditorBrush
                common={common}
                brush={editor.brush}
                setBrush={setEditorBrush} />
        </div>
    )

    const renderEditorLocation = location => (
        <div>
            <h4 className="text-center">Location ({location.x},{location.y})</h4>
            <EditorLocation
                common={common}
                machinery={value.getIn(['content', 'machinery'])}
                content={contents.get(location, LocationContent())}
                setContent={setLocationContent(location)} />
        </div>
    )

    return (
        <div className="clearfix">
            <div className="pull-left">
                {editorView}
            </div>
            <div className="pull-right" style={{minWidth: '50%'}}>
                {editorBrush}
                {editor.location && renderEditorLocation(editor.location)}
            </div>
        </div>
    )
}

export default Editor
