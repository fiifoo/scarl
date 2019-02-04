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
        )

        this.screen.build()
        this.screen.update(this.props.world.system)
    }

    UNSAFE_componentWillReceiveProps(nextProps) {
        this.screen.update(nextProps.world.system)
    }
}

export default System
