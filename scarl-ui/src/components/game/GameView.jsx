import React, { Component } from 'react'

class GameView extends Component {

    shouldComponentUpdate(nextProps) {
        return ! this.props.lazy || this.props.visible || nextProps.visible
    }

    render() {
        // eslint-disable-next-line no-unused-vars
        const {component, lazy, scrollable = true, visible, ...props} = this.props
        const Component = component
        const className = scrollable ? 'game-view scrollable' : 'game-view'

        return (
            <div className={className} style={{display: visible ? 'block' : 'none'}}>
                <Component {...props} />
            </div>
        )
    }
}

GameView.defaultProps = {
    lazy: true,
}

export default GameView
