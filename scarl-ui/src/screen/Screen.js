const COLS = 80
const ROWS = 25

export default (element, kinds) => {

    const build = map => {
        const fragment = document.createDocumentFragment()

        for (let y = 0; y < ROWS; y++) {
            const row = document.createElement('tr')
            fragment.appendChild(row)
            for (let x = 0; x < COLS; x++) {
                const cell = document.createElement('td')
                if (map[x] !== undefined && map[x][y] !== undefined) {
                    renderMapCell(cell, map[x][y], kinds)
                }
                row.appendChild(cell)
            }
        }

        element.appendChild(fragment)
    }

    const reset = map => {
        while (element.lastChild) {
            element.removeChild(element.lastChild)
        }

        build(map)
    }

    const update = fov => {
        fov.delta.forEach((rows, x) => {
            rows.forEach((entities, y) => {
                if (element.rows[y] !== undefined && element.rows[y].cells[x] !== undefined) {
                    const cell = element.rows[y].cells[x]

                    renderCell(cell, entities)
                }
            })
        })
        fov.shouldHide.forEach(location => {
            const x = location.x
            const y = location.y
            if (element.rows[y] !== undefined && element.rows[y].cells[x] !== undefined) {
                const cell = element.rows[y].cells[x]
                const entities = fov.cumulative[x][y]

                renderCell(cell, entities, false)
            }
        })
    }

    const updateCursor = (next, previous) => {
        if (previous !== null) {
            hideCursor(previous)
        }
        if (next !== null) {
            showCursor(next)
        }
    }

    const showCursor = location => {
        const cell = getCell(location)

        if (cell !== null) {
            cell.className = 'cursor'
        }
    }

    const hideCursor = location => {
        const cell = getCell(location)

        if (cell !== null) {
            cell.className = ''
        }
    }

    const renderCell = (cell, entities, visible = true) => {
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

    const renderMapCell = (cell, data) => {
        if (data.items.length > 0) {
            renderKind(cell, data.items[data.items.length - 1], kinds.items)
        } else if (data.wall) {
            renderKind(cell, data.wall, kinds.walls)
        } else if (data.terrain) {
            renderKind(cell, data.terrain, kinds.terrains)
        } else {
            renderEmpty(cell)
        }
    }

    const renderEntity = (cell, entity, kindsBranch) => {
        renderKind(cell, entity.kind, kindsBranch)
    }

    const renderKind = (cell, kindId, branch) => {
        const kind = branch.get(kindId)
        const display = kind.display
        const color = kind.color

        cell.innerHTML = '<div style="color: ' + color + '">' + display + '</div>'
    }

    const renderEmpty = (cell) => {
        cell.innerHTML = '.'
    }

    const getCell = ({x, y}) => {
        if (element.rows[y] !== undefined && element.rows[y].cells[x] !== undefined) {
            return element.rows[y].cells[x]
        } else {
            return null
        }
    }

    return {
        build,
        reset,
        update,
        updateCursor,
    }
}
