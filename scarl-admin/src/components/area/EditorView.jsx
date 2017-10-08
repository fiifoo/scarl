import { is, Range } from 'immutable'
import React from 'react'
import Location from '../../data/area/Location'
import LocationContent from '../../data/area/LocationContent'
import { calculateDimensions } from '../../data/area/Shape'

const emptyContent = LocationContent()
const conduitBackgroundColor = '#444'
const gatewayBackgroundColor = '#777'
const machineryControlBorderColor = 'red'
const machineryTargetBorderColor = 'blue'
const templateLocationBorderColor = '#fff'

const EditorView = ({common, contents, editor, shape, setEditorLocation, setLocationContent}) => {
    const {data} = common

    const dimensions = calculateDimensions(shape)
    const width = dimensions.innerWidth
    const height = dimensions.innerHeight

    const onLocationClick = location => () => {
        const brush = editor.brush
        if (brush.property) {
            const content = contents.get(location, emptyContent).set(brush.property, brush.value)

            setLocationContent(location)(content)
        } else {
            setEditorLocation(location)
        }
    }

    const renderRow = y => {
        const cell = renderCell(y)

        return (
            <tr key={y}>
                {Range(0, width).map(cell)}
            </tr>
        )
    }

    const renderCell = y => x => {
        const location = Location({x, y})
        const content = contents.get(location, emptyContent)

        const display = renderLocationContent(content)
        const backgroundColor = getLocationBackgroundColor(content)
        const borderColor = getLocationBorderColor(content)
        const selected = editor.location ? is(location, editor.location) : false

        return (
            <td key={x}
                onClick={onLocationClick(location)}
                className={selected ? 'selected' : null}
                style={{backgroundColor, borderColor}}>
                {display}
            </td>
        )
    }

    const renderLocationContent = content => {
        if (content.creature) {
            return renderKind(content.creature, 'creatures')
        } else if (content.entrance) {
            return renderKind(content.entrance, 'items')
        } else if (content.widget) {
            return renderWidget(content.widget)
        } else if (content.items.size > 0) {
            return renderKind(content.items.last(), 'items')
        } else if (content.wall) {
            return renderKind(content.wall, 'walls')
        } else if (content.terrain) {
            return renderKind(content.terrain, 'terrains')
        } else {
            return null
        }
    }

    const renderWidget = id => {
        const widget = data.getIn(['kinds', 'widgets', id])
        const itemId = widget.getIn(['data', 'item'])

        return renderKind(itemId, 'items')
    }

    const renderKind = (id, branch) => {
        const kind = data.getIn(['kinds', branch, id])
        const display = kind.get('display')
        const color = kind.get('color')

        return (
            <div style={{color}}>{display}</div>
        )
    }

    const getLocationBackgroundColor = content => {
        if (content.gateway) {
            return gatewayBackgroundColor
        } else if (content.conduit) {
            return conduitBackgroundColor
        } else {
            return null
        }
    }

    const getLocationBorderColor = content => {
        if (content.templateLocation) {
            return templateLocationBorderColor
        } else if (content.machineryControls.size > 0) {
            return machineryControlBorderColor
        } else if (content.machineryTargets.size > 0) {
            return machineryTargetBorderColor
        } else {
            return null
        }
    }

    return (
        <table className="area-editor">
            <tbody>
                {Range(0, height).map(renderRow)}
            </tbody>
        </table>
    )
}

export default EditorView
