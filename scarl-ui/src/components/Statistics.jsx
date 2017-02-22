import React from 'react'

const Statistics = ({kinds, statistics}) => (
    <div>
        <h4>Creatures that met their end:</h4>
        {statistics.deaths.map((count, kind) => (
            <div>{kinds.creatures.get(kind).name}: {count}</div>
        ))}
    </div>
)

export default Statistics
