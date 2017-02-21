import React, { Component } from 'react'
import { COLS, ROWS } from '../const/view'

import './View.css'

const build = element => {
    const fragment = document.createDocumentFragment()

    for (let y = 0; y < ROWS; y++) {
        const row = document.createElement('tr')
        fragment.appendChild(row)
        for (let x = 0; x < COLS; x++) {
            const cell = document.createElement('td')
            row.appendChild(cell)
        }
    }

    element.appendChild(fragment)
}

const reset = element => {
    while (element.lastChild) {
        element.removeChild(element.lastChild)
    }
    build(element)
}

const update = (element, fov, kinds) => {
    fov.delta.forEach((rows, x) => {
        rows.forEach((entities, y) => {
            if (element.rows[y] !== undefined && element.rows[y].cells[x] !== undefined) {
                const cell = element.rows[y].cells[x]
                renderCell(cell, entities, kinds)
            }
        })
    })
    fov.shouldHide.forEach(location => {
        const x = location.x
        const y = location.y
        if (element.rows[y] !== undefined && element.rows[y].cells[x] !== undefined) {
            const cell = element.rows[y].cells[x]
            const entities = fov.cumulative[x][y]
            renderCell(cell, entities, kinds, false)
        }
    })
}

const renderCell = (cell, entities, kinds, visible = true) => {
    if (visible && entities.creature) {
        renderEntity(cell, entities.creature, kinds.creatures)
    } else if (entities.items.length > 0) {
        renderEntity(cell, entities.items[entities.items.length - 1], kinds.items)
    } else if (entities.wall) {
        renderEntity(cell, entities.wall, kinds.walls)
    } else if (entities.terrain) {
        renderEntity(cell, entities.terrain, kinds.terrains)
    } else {
        renderEmpty(cell)
    }
}

const renderEntity = (cell, entity, kinds) => {
    const kind = kinds.get(entity.kind)
    const display = kind.display
    const color = kind.color
    cell.innerHTML = '<div style="color: ' + color + '">' + display + '</div>'
}

const renderEmpty = (cell) => {
    cell.innerHTML = '.'
}

class View extends Component {

    shouldComponentUpdate() {
        return false
    }

    componentDidMount() {
        build(this.element)
        update(this.element, this.props.fov, this.props.kinds)
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.connection === false) {
            reset(this.element)
        } else {
            update(this.element, nextProps.fov, nextProps.kinds)
        }
    }

    render() {
        return (
            <table className="view" onClick={this.props.focusKeyboard}>
                <tbody ref={tbody => this.element = tbody} />
            </table>
        )
    }
}

export default View
