import React from 'react'
import MessageLog from './MessageLog.jsx'

const GameOver = ({events, kinds, statistics}) => (
    <div>
        <h3>Your journey is over</h3>
        <h4>Creatures that met their end:</h4>
        {statistics.deaths.map((count, kind) => (
            <div key={kind}>{kinds.creatures.get(kind).name}: {count}</div>
        )).toArray()}
        <MessageLog events={events} />
    </div>
)

export default GameOver
