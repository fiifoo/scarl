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
            this.props.actions,
        )

        this.screen.build()
        this.screen.update(this.props.world, this.props.ui, this.props.viewSize)
    }

    UNSAFE_componentWillReceiveProps(nextProps) {
        if (nextProps.ui.travel && nextProps.ui.travel.simulate) {
            if (nextProps.ui.travel !== this.props.ui.travel) {
                this.screen.simulateTravel(nextProps.world, nextProps.ui)
            }
        } else {
            if (nextProps.world !== this.props.world || nextProps.ui !== this.props.ui) {
                this.screen.update(nextProps.world, nextProps.ui, this.props.viewSize)
            }
        }
    }
}

export default System
