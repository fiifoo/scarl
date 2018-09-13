import React, { Component } from 'react'
import createScreen from '../../screen'
import { TILE_SIZE } from '../../screen/const'

const position = (element, offset) => {
    const left = -offset.x * TILE_SIZE
    const top = -offset.y * TILE_SIZE

    element.style.marginLeft = left + 'px'
    element.style.marginTop = top + 'px'
}

class Screen extends Component {

    constructor(props) {
        super(props)
        this.ref = React.createRef()
    }

    shouldComponentUpdate() {
        return false
    }

    render() {
        return (
            <div className="screen" ref={this.ref} />
        )
    }

    componentDidMount() {
        this.screen = createScreen(this.ref.current, this.props.kinds, this.props.autoMove)

        position(this.ref.current, this.props.offset)
        this.screen.build(this.props.area)
        this.screen.update(this.props.fov, this.props.events, this.props.player)
    }

    UNSAFE_componentWillReceiveProps(nextProps) {
        position(this.ref.current, nextProps.offset)

        if (nextProps.area.id !== this.props.area.id) {
            this.screen.reset(nextProps.area)
        }

        if (nextProps.debugMode !== this.props.debugMode || nextProps.debug !== this.props.debug) {
            this.screen.updateDebug(nextProps.debugMode, nextProps.debug)
        } else if (nextProps.cursor !== this.props.cursor) {
            this.screen.updateCursor(nextProps.cursor)
        } else if (nextProps.reticule !== this.props.reticule) {
            this.screen.updateReticule(nextProps.reticule, nextProps.trajectory)
        } else {
            this.screen.update(nextProps.fov, nextProps.events, nextProps.player)
        }
    }
}

export default Screen
