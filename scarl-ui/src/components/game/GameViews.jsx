import React, { Component } from 'react'
import CraftingContainer from './CraftingContainer'
import InventoryContainer from '../inventory/InventoryContainer'
import GameOverContainer from './GameOverContainer'
import KeyBindingsContainer from './KeyBindingsContainer'
import MessageLogContainer from './MessageLogContainer'
import PlayerInfoContainer from './PlayerInfoContainer'
import ScreenContainer from './ScreenContainer'

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

                        <CraftingContainer />
                        <GameOverContainer />
                        <InventoryContainer />
                        <KeyBindingsContainer />
                        <MessageLogContainer />
                        <PlayerInfoContainer />
                    </React.Fragment>
                )}
            </div>
        )
    }
}

export default GameViews
