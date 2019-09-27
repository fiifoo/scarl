import { Set } from 'immutable'
import React from 'react'
import { getStanceLabel, getStanceDescriptions, getStanceSelection, isChangeStanceAllowed } from '../../game/stance'
import { getPlayerStats } from '../../game/utils'

const containerStyle = {
    marginTop: 20
}
const statusStyle = {
    marginBottom: 30,
    textAlign: 'center',
}
const rowStyle = {
    display: 'flex',
}
const choiceStyle = {
    width: 200,
    height: 100,
    textAlign: 'center',
}
const activeChoiceStyle = {
    ...choiceStyle,
    color: '#31708f',
}
const disabledChoiceStyle = {
    ...choiceStyle,
    color: '#777777',
}
const stanceLabelStyle = {
    marginBottom: 6,
}

const getChoiceStyle = (isActive, isDisabled) => (
    isActive ? activeChoiceStyle : (isDisabled ? disabledChoiceStyle : choiceStyle)
)

const Choice = ({isActive, isDisabled, number, stance}) => (
    <div style={getChoiceStyle(isActive, isDisabled)}>
        <div>
            <b style={stanceLabelStyle}>{number}. {getStanceLabel(stance)}</b>
        </div>
        {getStanceDescriptions(stance).map((description, index) => (
            <div key={index}><i>{description}</i></div>
        ))}
    </div>
)

const EmptyChoice = ({number}) => (
    <div style={choiceStyle}>
        <i className="text-muted">{number}. empty</i>
    </div>
)

const ClearChoice = ({isActive, isDisabled, number}) => (
    <div style={getChoiceStyle(isActive, isDisabled)}>
        <b>{number}. Normal</b>
    </div>
)

const Stance = ({player}) => {
    const stances = getPlayerStats(player).stances
    const activeStances = Set(player.creature.stances.map(x => x.key))
    const isDisabled = ! isChangeStanceAllowed(player.creature)
    const getSelection = getStanceSelection(stances)

    const statusLabel = <span>Current stance: </span>

    const status = activeStances.isEmpty() ? (
        <h4 style={statusStyle}>
            {statusLabel}
            <span>Normal</span>
        </h4>
    ) : (
        player.creature.stances.map((info, index) => (
            <h4 style={statusStyle} key={index}>
                {statusLabel}
                <span className="text-info">{getStanceLabel(info.key)}</span>
                {info.duration && (
                    <span className={info.duration !== info.initialDuration ? 'text-danger' : null}>
                        &nbsp;(cooldown {info.duration} {info.duration === 1 ? 'turn' : 'turns'})
                    </span>
                )}
            </h4>
        ))
    )

    const renderChoice = number => {
        const stance = getSelection(number)

        return (stance ? (
            <Choice isActive={activeStances.contains(stance)} isDisabled={isDisabled} number={number} stance={stance} />
        ) : (
            <EmptyChoice number={number} />
        ))
    }

    return (
        <div style={containerStyle}>
            {status}
            <div style={rowStyle}>
                {renderChoice(7)}
                {renderChoice(8)}
                {renderChoice(9)}
            </div>
            <div style={rowStyle}>
                {renderChoice(4)}
                <ClearChoice isActive={activeStances.isEmpty()} isDisabled={isDisabled} number={5} />
                {renderChoice(6)}
            </div>
            <div style={rowStyle}>
                {renderChoice(1)}
                {renderChoice(2)}
                {renderChoice(3)}
            </div>
        </div>
    )
}

export default Stance
