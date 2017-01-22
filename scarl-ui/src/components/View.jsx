import React, { Component } from 'react'
import './View.css'

const rows = Array(25)
rows.fill(null)
const cols = Array(80)
cols.fill(null)

const buildMap = creatures => {
    const map = []

    creatures.forEach(creature => {
        const x = creature.location.x
        const y = creature.location.y

        if (! map[y]) {
            map[y] = []
        }
        if (! map[y][x]) {
            map[y][x] = []
        }
        map[y][x].push(creature)
    })

    return map
}

const buildFovMap = fov => {
    const map = []

    fov.forEach(location => {
        const x = location.x
        const y = location.y

        if (! map[y]) {
            map[y] = []
        }
        if (! map[y][x]) {
            map[y][x] = true
        }
    })

    return map
}

class ViewCell extends Component {
    shouldComponentUpdate(nextProps) {
        return this.props.display !== nextProps.display || this.props.seen !== nextProps.seen
    }

    render() {
        const {display, move, seen, x, y} = this.props

        return (
            <td onClick={() => move({x, y})} className={seen ? 'seen' : null}>
                {display}
            </td>
        )
    }
}

const View = ({creatures, fov, move}) => {
    const map = buildMap(creatures)
    const fovMap = buildFovMap(fov)

    const renderCell = (y, x) => {
        const display = map[y] && map[y][x] ? 'x' : null
        const seen = fovMap[y] && fovMap[y][x]

        return <ViewCell key={x} display={display} move={move} seen={seen} x={x} y={y} />
    }

    return (
        <table className="view">
            <tbody>
                {rows.map((_, y) =>
                    <tr key={y}>
                        {cols.map((_, x) => renderCell(y, x))}
                    </tr>
                )}
            </tbody>
        </table>
    )
}

export default View
