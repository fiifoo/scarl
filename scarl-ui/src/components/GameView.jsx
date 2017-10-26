import React, { Component } from 'react'

class GameView extends Component {

    shouldComponentUpdate(nextProps) {
        return ! this.props.lazy || this.props.visible || nextProps.visible
    }

    render() {
        // eslint-disable-next-line no-unused-vars
        const {component, lazy, visible, ...props} = this.props
        const Component = component

        return (
            <div className="game-view" style={{display: visible ? 'block' : 'none'}}>
                <Component {...props} />
            </div>
        )
    }
}

export default GameView
