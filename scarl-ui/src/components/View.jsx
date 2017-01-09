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

class ViewCell extends Component {
    shouldComponentUpdate(nextProps) {
        return this.props.display !== nextProps.display
    }

    render() {
        return <td>{this.props.display}</td>
    }
}

const View = ({creatures}) => {
    const map = buildMap(creatures)

    const renderCell = (y, x) => {
        const display = map[y] && map[y][x] ? 'x' : null

        return <ViewCell key={x} display={display} />
    }

    return (
        <table className="table table-bordered table-condensed view">
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
