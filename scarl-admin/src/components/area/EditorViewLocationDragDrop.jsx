import { Map } from 'immutable'
import React from 'react'
import { DragSource, DropTarget } from 'react-dnd'
import { getEmptyImage } from 'react-dnd-html5-backend'
import Location from '../../data/area/Location'
import LocationContent from '../../data/area/LocationContent'
import { calculateRectangle } from '../../data/area/Rectangle'
import EditorViewLocation from './EditorViewLocation.jsx'

const emptyContent = LocationContent()
const dragType = 'Location'

const backgroundColor = 'rgb(0, 255, 255, 0.25)'

class EditorViewLocationDragDrop extends React.Component {
    componentDidMount() {
        const { connectDragPreview } = this.props
        connectDragPreview(getEmptyImage())
    }

    render() {
        const {connectDragSource, connectDropTarget, isDragging, isOver, ...props} = this.props

        return connectDropTarget(connectDragSource(
            <td
                ref={element => this.element = element}
                style={{
                    backgroundColor: isDragging || isOver ? backgroundColor : null,
                }}>
                <EditorViewLocation {...props} />
            </td>
        ))
    }
}

const selectLocations = ({location, setEditorLocation}, item) => {
    const source = item.location
    const target = location
    const locations = calculateRectangle(source, target)

    setEditorLocation(location, locations)
}

const moveLocations = ({contents, editor, location, setContents, setEditorLocation}, item) => {
    const source = item.location
    const target = location
    const dx = target.x - source.x
    const dy = target.y - source.y
    const sources = editor.locations

    const sourceContents = Map(sources.toArray().map(l => [l, emptyContent]))
    const targetContents = Map(sources.toArray().map(source => {
        const sourceContent = contents.get(source, emptyContent)
        const target = Location({x: source.x + dx, y: source.y + dy})
        const targetContent = sources.contains(target) ? (
            sourceContent
        ) : (
            LocationContent.merge(sourceContent, contents.get(target, emptyContent))
        )

        return [target, targetContent]
    }))

    setContents(sourceContents.merge(targetContents))
    setEditorLocation(target, sources.map(source => Location({x: source.x + dx, y: source.y + dy})))
}

const dragSource = {
    beginDrag: ({editor, location}, monitor, component) => ({
        location,
        size: {
            width: component.element.offsetWidth,
            height: component.element.offsetHeight,
        },
        select: !editor.locations.contains(location),
    }),
}

const dropTarget = {
    drop: (props, monitor) => {
        const item = monitor.getItem()
        if (item.select) {
            selectLocations(props, item)
        } else {
            moveLocations(props, item)
        }
    }
}

const collectDrag = (connect, monitor) => ({
    connectDragSource: connect.dragSource(),
    connectDragPreview: connect.dragPreview(),
    isDragging: monitor.isDragging(),
})

const collectDrop = (connect, monitor) => ({
    connectDropTarget: connect.dropTarget(),
    isOver: monitor.isOver(),
})

export default DropTarget(dragType, dropTarget, collectDrop)(
    DragSource(dragType, dragSource, collectDrag)(
        EditorViewLocationDragDrop
    ))
