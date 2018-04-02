import React, { Component } from 'react'
import GameOverContainer from '../containers/GameOverContainer'
import InventoryContainer from '../containers/InventoryContainer'
import KeyBindingsContainer from '../containers/KeyBindingsContainer'
import MessageLogContainer from '../containers/MessageLogContainer'
import ScreenContainer from '../containers/ScreenContainer'

class GameViews extends Component {

    constructor(props) {
        super(props)
        this.ref = React.createRef()

        this.resize = () => {
            const element = this.ref.current
            const height = window.innerHeight - element.offsetTop

            element.style.height = height + 'px'

            this.props.storeGameViewSize({
                width: element.offsetWidth,
                height: element.offsetHeight,
            })
        }
    }

    componentDidMount() {
        this.resize()

        window.addEventListener('resize', this.resize)
    }

    componentWillUnmount() {
        window.removeEventListener('resize', this.resize)
    }

    render() {
        const {size} = this.props

        return (
            <div ref={this.ref}>
                {size === null ? (
                    null
                ) : (
                    <React.Fragment>
                        <ScreenContainer size={size} />
                        <InventoryContainer />
                        <KeyBindingsContainer />
                        <MessageLogContainer />
                        <GameOverContainer />
                    </React.Fragment>
                )}
            </div>
        )
    }
}

export default GameViews
