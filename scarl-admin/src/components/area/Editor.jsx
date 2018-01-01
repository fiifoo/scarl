import React from 'react'
import LocationContent from '../../data/area/LocationContent'
import EditorBrush from './EditorBrush.jsx'
import EditorLocation from './EditorLocation.jsx'
import EditorView from './EditorView.jsx'

import './Editor.css'

const Editor = ({path, value, common, editor, setEditorBrush, setEditorLocation}) => {
    const {data, setValue} = common

    if (! value.getIn(['shape', 'type'])) {
        return <div />
    }

    const contents = LocationContent.read(value, data)
    const setLocationContent = location => content => setValue(path, LocationContent.write(value, location, content))
    const setContents = contents => {
        const next = contents.reduce((value, content, location) => (
            LocationContent.write(value, location, content)
        ), value)

        setValue(path, next)
    }

    const editorView = (
        <div>
            <h4 className="text-center">Preview</h4>
            <EditorView
                common={common}
                contents={contents}
                editor={editor}
                shape={value.get('shape')}
                setEditorLocation={setEditorLocation}
                setContents={setContents} />
        </div>

    )

    const renderEditorBrush = () => (
        <div>
            <h4 className="text-center">Brush</h4>
            <EditorBrush
                common={common}
                contents={contents}
                editor={editor}
                setBrush={setEditorBrush}
                setContents={setContents} />
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
                {editor.locations.size > 1 && renderEditorBrush()}
                {editor.locations.size == 1 && renderEditorLocation(editor.location)}
            </div>
        </div>
    )
}

export default Editor
