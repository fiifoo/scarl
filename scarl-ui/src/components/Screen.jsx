import React, { Component } from 'react'
import createScreen from '../screen/Screen.js'

import './Screen.css'

class Screen extends Component {

    shouldComponentUpdate() {
        return false
    }

    componentDidMount() {
        this.screen = createScreen(this.element, this.props.kinds)

        this.screen.build(this.props.map)
        this.screen.update(this.props.fov)
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.game.started === false) {
            this.screen.reset(nextProps.map)
        } else {
            if (nextProps.area !== this.props.area) {
                this.screen.reset(nextProps.map)
            }
            this.screen.update(nextProps.fov)
        }
    }

    render() {
        return (
            <table className="screen" onClick={this.props.focusKeyboard}>
                <tbody ref={tbody => this.element = tbody} />
            </table>
        )
    }
}

export default Screen
