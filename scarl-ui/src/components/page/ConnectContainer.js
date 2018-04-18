import React from 'react'
import { connect } from 'react-redux'
import Connect from './Connect.jsx'

const ComponentIf = ({connection}) => (
    !connection ? (
        <Connect />
    ) : (
        <div />
    )
)

const ConnectContainer = connect(
    state => ({
        connection: state.connection,
    })
)(ComponentIf)

export default ConnectContainer
