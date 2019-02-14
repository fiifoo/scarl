import React, { Component } from 'react'
import createScreen from '../../system/screen'

class System extends Component {

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
        this.screen = createScreen(
            this.ref.current,
            this.props.spaceships,
            this.props.stellarBodies,
            this.props.clearTravel,
        )

        this.screen.build()
        this.screen.update(this.props.world.system, this.props.ui)
    }

    UNSAFE_componentWillReceiveProps(nextProps) {
        if (nextProps.ui.travel && nextProps.ui.travel.simulate) {
            if (nextProps.ui.travel !== this.props.ui.travel) {
                this.screen.simulateTravel(nextProps.world.system, nextProps.ui)
            }
        } else {
            if (nextProps.world.system !== this.props.world.system || nextProps.ui !== this.props.ui) {
                this.screen.update(nextProps.world.system, nextProps.ui)
            }
        }
    }
}

export default System
