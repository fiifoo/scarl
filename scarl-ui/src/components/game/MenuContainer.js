import React from 'react'
import { connect } from 'react-redux'
import { focusKeyboard } from '../../actions/keyboard'
import { cancelMode } from '../../actions/gameActions'
import { MENU } from '../../game/modes'
import Menu from './Menu.jsx'

const ComponentIf = ({visible, ...props}) => (
    visible ? (
        <Menu {...props} />
    ) : (
        <div />
    )
)

const MenuContainer = connect(
    state => ({
        visible: state.ui.game.mode === MENU,
    }), {
        cancelMode,
        focusKeyboard,
    }
)(ComponentIf)

export default MenuContainer
