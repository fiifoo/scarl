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

const update = (element, fov, player) => {
    fov.delta.forEach((rows, x) => {
        rows.forEach((entities, y) => {
            if (element.rows[y] !== undefined && element.rows[y].cells[x] !== undefined) {
                const cell = element.rows[y].cells[x]
                renderVisible(cell, entities, player)
            }
        })
    })
    fov.shouldHide.forEach(location => {
        const x = location.x
        const y = location.y
        if (element.rows[y] !== undefined && element.rows[y].cells[x] !== undefined) {
            const cell = element.rows[y].cells[x]
            const entities = fov.cumulative[x][y]
            renderHidden(cell, entities)
        }
    })
}

const renderVisible = (cell, entities, player) => {
    if (entities.creature) {
        renderCreature(cell, entities.creature, player)
    } else if (entities.wall) {
        renderWall(cell)
    } else {
        renderTerrain(cell)
    }
}

const renderHidden = (cell, entities) => {
    if (entities.wall) {
        renderWall(cell)
    } else {
        renderTerrain(cell)
    }
}

const renderCreature = (cell, creature, player) => {
    if (creature.id === player.id) {
        cell.innerHTML = '<div style="color: yellow;">@</div>'
    } else {
        cell.innerHTML = '<div style="color: green;">o</div>'
    }
}

const renderWall = (cell) => {
    cell.innerHTML = '<div style="color: darkgrey;">#</div>'
}

const renderTerrain = (cell) => {
    cell.innerHTML = '.'
}

class View extends Component {

    shouldComponentUpdate() {
        return false
    }

    componentDidMount() {
        build(this.element)
        update(this.element, this.props.fov, this.props.player)
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.connection === false) {
            reset(this.element)
        } else {
            update(this.element, nextProps.fov, nextProps.player)
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
