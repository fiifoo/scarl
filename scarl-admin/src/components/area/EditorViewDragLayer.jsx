
import React from 'react'
import { DragLayer } from 'react-dnd'

const layerStyle = {
    position: 'fixed',
    pointerEvents: 'none',
    zIndex: 100,
    left: 0,
    top: 0,
    width: '100%',
    height: '100%',
}

const margin = 2

const getStyle = (left, top, width, height) => ({
    left: left - margin,
    top: top - margin,
    width: width + margin * 2,
    height: height + margin * 2,
    border: '1px solid #fff',
    position: 'fixed',
})

const getSelectStyle = (from, to, size) => {
    const dx = Math.abs(to.x - from.x)
    const dy = Math.abs(to.y - from.y)
    const snap = {
        x: size.width - dx % size.width,
        y: size.height - dy % size.height,
    }

    const left = from.x <= to.x ? from.x : to.x - snap.x
    const top = from.y <= to.y ? from.y : to.y - snap.y
    const width = from.x <= to.x ? dx + snap.x : dx + snap.x + size.width
    const height = from.y <= to.y ? dy + snap.y : dy + snap.y + size.height

    return getStyle(left, top, width, height)
}

const getMoveStyle = (locations, location, from, to, size) => {
    const min = {
        x: locations.reduce((result, l) => (
            result === null || l.x < result ? l.x : result
        ), locations.first().x),
        y: locations.reduce((result, l) => (
            result === null || l.y < result ? l.y : result
        ), locations.first().y),
    }

    const max = {
        x: locations.reduce((result, l) => (
            result === null || l.x > result ? l.x : result
        ), locations.first().x),
        y: locations.reduce((result, l) => (
            result === null || l.y > result ? l.y : result
        ), locations.first().y),
    }

    const move = {
        x: Math.floor((to.x - from.x) / size.width) * size.width,
        y: Math.floor((to.y - from.y) / size.height) * size.height,
    }

    const left = from.x - (location.x - min.x) * size.width + move.x
    const top = from.y - (location.y - min.y) * size.height + move.y
    const width = (max.x - min.x) * size.width + size.width
    const height = (max.y - min.y) * size.height + size.height

    return getStyle(left, top, width, height)
}

class EditorViewDragLayer extends React.Component {
    render() {
        const { locations, isDragging, item, from, to} = this.props

        if (! isDragging || ! to) {
            return null
        }

        const { location, size, select } = item
        const style = select ? getSelectStyle(from, to, size) : getMoveStyle(locations, location, from, to, size)

        return (
            <div style={layerStyle}>
                <div style={style} />
            </div>
        )
    }
}

const collect = monitor => ({
    isDragging: monitor.isDragging(),
    item: monitor.getItem(),
    from: monitor.getInitialSourceClientOffset(),
    to: monitor.getClientOffset(),
})

export default DragLayer(collect)(EditorViewDragLayer)
