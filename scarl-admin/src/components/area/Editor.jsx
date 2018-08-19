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

    const machinery = value.getIn(['content', 'machinery'])
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
                machinery={machinery}
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
                machinery={machinery}
                content={contents.get(location, LocationContent())}
                setContent={setLocationContent(location)} />
        </div>
    )

    return (
        <div>
            {editorView}
            {editor.locations.size > 1 && renderEditorBrush()}
            {editor.locations.size == 1 && renderEditorLocation(editor.location)}
        </div>
    )
}

export default Editor
