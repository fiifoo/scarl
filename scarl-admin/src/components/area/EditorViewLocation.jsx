import { Set } from 'immutable'
import React from 'react'
import LocationContent from '../../data/area/LocationContent'
import { calculateRectangle } from '../../data/area/Rectangle'

const emptyContent = LocationContent()
const conduitColor = 'green'
const gatewayColor = 'yellow'
const machineryControlColor = 'red'
const machineryTargetColor = 'blue'
const templateLocationColor = 'white'
const restrictedColor = '#555'

const EditorViewLocation = ({common, contents, editor, location, setEditorLocation}) => {
    const {data} = common

    const content = contents.get(location, emptyContent)

    const display = renderContent(data, content)
    const borderColor = getBorderColor(content)
    const selected = editor.locations.contains(location)

    const onClick = event => {
        if (event.shiftKey) {
            const add = calculateRectangle(editor.location, location)

            setEditorLocation(location, editor.locations.union(add))
        } else if (event.ctrlKey) {
            if (editor.locations.contains(location)) {
                setEditorLocation(editor.location, editor.locations.remove(location))
            } else {
                setEditorLocation(location, editor.locations.add(location))
            }
        } else {
            setEditorLocation(location, Set([location]))
        }
    }

    return (
        <div onMouseDown={onMouseDown}
            onClick={onClick}
            className={selected ? 'selected' : null}
            style={{borderColor}}>
            {display}
        </div>
    )
}

const onMouseDown = event => {
    if (event.shiftKey || event.ctrlKey) {
        event.preventDefault()
    }
}

const renderContent = (data, content) => {
    if (content.creature) {
        return renderKind(data, content.creature, 'creatures')
    } else if (content.entrance) {
        return renderKind(data, content.entrance, 'items')
    } else if (content.widget) {
        return renderWidget(data, content.widget)
    } else if (content.items.size > 0) {
        return renderKind(data, content.items.last(), 'items')
    } else if (content.wall) {
        return renderKind(data, content.wall, 'walls')
    } else if (content.terrain) {
        return renderKind(data, content.terrain, 'terrains')
    } else {
        return '\u00A0'
    }
}

const renderWidget = (data, id) => {
    const widget = data.getIn(['kinds', 'widgets', id])
    const itemId = widget.getIn(['data', 'item'])

    return renderKind(data, itemId, 'items')
}

const renderKind = (data, id, branch) => {
    const kind = data.getIn(['kinds', branch, id])
    const display = kind.get('display')
    const color = kind.get('color')

    return (
        <div style={{color}}>{display}</div>
    )
}

const getBorderColor = content => {
    if (content.gateway) {
        return gatewayColor
    } else if (content.conduit !== undefined) {
        return conduitColor
    } else if (content.templateLocation) {
        return templateLocationColor
    } else if (content.machineryControls.size > 0) {
        return machineryControlColor
    } else if (content.machineryTargets.size > 0) {
        return machineryTargetColor
    } else if (content.restricted) {
        return restrictedColor
    } else {
        return null
    }
}

export default EditorViewLocation
