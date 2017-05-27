import React, { Component } from 'react'
import createScreen from '../screen'

class Screen extends Component {

    shouldComponentUpdate() {
        return false
    }

    componentDidMount() {
        this.screen = createScreen(this.element, this.props.kinds)

        this.screen.build(this.props.area.map)
        this.screen.update(this.props.fov)
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.game.running === false) {
            this.screen.reset(nextProps.area.map)
        } else {
            if (nextProps.area.id !== this.props.area.id) {
                this.screen.reset(nextProps.area.map)
            }

            if (nextProps.cursor !== this.props.cursor) {
                this.screen.updateCursor(nextProps.cursor)
            } else if (nextProps.reticule !== this.props.reticule) {
                this.screen.updateReticule(nextProps.reticule, nextProps.trajectory)
            } else {
                this.screen.update(nextProps.fov)
            }
        }
    }

    render() {
        return (
            <div className="screen" ref={element => this.element = element} />
        )
    }
}

export default Screen
