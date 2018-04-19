import React from 'react'
import { connect } from 'react-redux'
import { MENU } from '../../game/modes'
import Menu from './Menu.jsx'

const ComponentIf = ({visible}) => (
    visible ? (
        <Menu />
    ) : (
        <div />
    )
)

const MenuContainer = connect(
    state => ({
        visible: state.ui.game.mode === MENU,
    })
)(ComponentIf)

export default MenuContainer
