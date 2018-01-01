import { Range } from 'immutable'
import React from 'react'
import { DragDropContext } from 'react-dnd'
import HTML5Backend from 'react-dnd-html5-backend'
import Location from '../../data/area/Location'
import { calculateDimensions } from '../../data/area/Shape'
import EditorViewDragLayer from './EditorViewDragLayer.jsx'
import EditorViewLocationDragDrop from './EditorViewLocationDragDrop.jsx'

const EditorView = ({common, contents, editor, shape, setEditorLocation, setContents}) => {
    const dimensions = calculateDimensions(shape)
    const width = dimensions.innerWidth
    const height = dimensions.innerHeight

    const renderRow = y => {
        const cell = renderCell(y)

        return (
            <tr key={y}>
                {Range(0, width).map(cell)}
            </tr>
        )
    }

    const renderCell = y => x => (
        <EditorViewLocationDragDrop
            key={x}
            common={common}
            contents={contents}
            editor={editor}
            location={Location({x, y})}
            setEditorLocation={setEditorLocation}
            setContents={setContents} />
    )

    return (
        <table className="area-editor">
            <tbody>
                {Range(0, height).map(renderRow)}
            </tbody>
        </table>
    )
}

class DragDropEditorView extends React.Component {
    render() {
        return (
            <div>
                <EditorViewDragLayer locations={this.props.editor.locations} />
                <EditorView {...this.props} />
            </div>
        )
    }
}

export default DragDropContext(HTML5Backend)(DragDropEditorView)
