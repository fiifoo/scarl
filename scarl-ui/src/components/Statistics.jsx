import React from 'react'

const Statistics = ({kinds, statistics}) => (
    <div>
        <h3>Your journey is over</h3>
        <h4>Creatures that met their end:</h4>
        {statistics.deaths.map((count, kind) => (
            <div key={kind}>{kinds.creatures.get(kind).name}: {count}</div>
        )).toArray()}
    </div>
)

export default Statistics
