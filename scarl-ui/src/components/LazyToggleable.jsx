import React, { Component } from 'react'
import Toggleable from './Toggleable.jsx'

class LazyToggleable extends Component {

    shouldComponentUpdate(nextProps) {
        return this.props.visible || nextProps.visible
    }

    render() {
        return <Toggleable {...this.props} />
    }
}

export default LazyToggleable
