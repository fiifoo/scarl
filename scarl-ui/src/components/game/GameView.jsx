import React, { Component } from 'react'

class GameView extends Component {

    shouldComponentUpdate(nextProps) {
        return ! this.props.lazy || this.props.visible || nextProps.visible
    }

    render() {
        // eslint-disable-next-line no-unused-vars
        const {component, lazy, layer = false, scrollable = true, visible, ...props} = this.props
        const Component = component

        const classes = ['game-view']
        if (! visible) {
            classes.push('hidden')
        }
        if (layer) {
            classes.push('layer')
        }
        if (scrollable) {
            classes.push('scrollable')
        }

        return (
            <div className={classes.join(' ')}>
                <Component {...props} />
            </div>
        )
    }
}

GameView.defaultProps = {
    lazy: true,
}

export default GameView
