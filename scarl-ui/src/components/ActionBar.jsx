import React from 'react'
import { LOOK } from '../game/modes'

const className = 'btn btn-default'
const activeClassName = 'btn btn-warning'
const getClassName = active => active ? activeClassName : className

const LookButton = ({mode, cancelMode, look}) => {
    const active = mode === LOOK
    const onClick = active ? cancelMode : look

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            >Look</button>
    )
}

const TalkButton = ({communicate}) => (
    <button
        className={className}
        onClick={communicate}
        >Talk</button>
)

const ActionBar = ({mode, cancelMode, communicate, focusKeyboard, look}) =>  {

    const wrap = action => () => {
        action()
        focusKeyboard()
    }

    cancelMode = wrap(cancelMode)
    communicate = wrap(communicate)
    look = wrap(look)

    return (
        <div className="btn-toolbar">
            <LookButton mode={mode} cancelMode={cancelMode} look={look} />
            <TalkButton communicate={communicate} />
        </div>
    )
}

export default ActionBar
