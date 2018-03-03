import React from 'react'

const Category = ({category, data}) => (
    <tbody>
        <tr>
            <th>{category}</th>
            <th style={{width: 100}}>Power</th>
        </tr>
        {data
            .sortBy((_, key) => key, (a, b) => a < b ? -1 : 1)
            .map((power, item) => (
                <tr key={item}>
                    <td>{item}</td>
                    <td>{power}</td>
                </tr>
            ))
            .toArray()
        }
    </tbody>
)

const EquipmentCombatPowerSummary = ({equipmentCombatPower}) => (
    <table className="table table-condensed table-striped">
        {equipmentCombatPower
            .map((data, category) => (
                <Category key={category} category={category} data={data} />
            ))
            .toArray()
        }
    </table>
)

export default EquipmentCombatPowerSummary
