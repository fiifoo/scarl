import { Map, Range } from 'immutable'
import React from 'react'
import { Button } from 'react-bootstrap'
import { slots } from '../../game/equipment'

const Empty = () => <i>Empty</i>

const equipmentSets = Range(1, 4)

const PureEquipmentSetDropdown = ({selected, visible, toggleVisible, setEquipmentSet}) =>  (
    <div className="actions-dropdown">
        <div
            className="toggle"
            onClick={toggleVisible}>
            ▼
        </div>
        <div className={! visible ? 'menu closed' : 'menu'}>
            {equipmentSets.map(set => (
                <div
                    key={set}
                    className={set === selected ? 'active' : null}
                    onClick={() => {
                        toggleVisible()
                        setEquipmentSet(set)
                    }}>
                    Switch to weapons {set}
                </div>
            ))}
        </div>
    </div>
)

class EquipmentSetDropdown extends React.Component {
    constructor(props) {
        super(props)

        this.state = {visible: false}

        this.toggleVisible = () => this.setState({visible: ! this.state.visible})
    }

    render() {
        return <PureEquipmentSetDropdown {...this.props} {...this.state} toggleVisible={this.toggleVisible} />
    }
}

const Equipped = ({equipmentSet, equipments, inventory, kinds, selected, recycleItem = undefined, setEquipmentSet}) => {
    const renderSlot = slot =>  {
        const item = equipments.get(slot.key)

        return (
            <tr key={slot.key} className={item && item === selected ? 'active' : null}>
                <th className="text-right">{slot.label}</th>
                <td className="full-width">
                    {item
                        ? kinds.get(inventory.get(item).kind).name
                        : (<Empty />)
                    }
                </td>
                <td>
                    {item && recycleItem && (
                        <Button
                            bsSize="xsmall"
                            onClick={() => recycleItem(item)}>
                            Recycle
                        </Button>

                    )}
                </td>
            </tr>
        )
    }

    return (
        <div>
            <h4>Equipment ({equipmentSet})</h4>
            <EquipmentSetDropdown selected={equipmentSet} setEquipmentSet={setEquipmentSet} />
            <table className="scarl-table">
                <tbody>
                    {Map(slots).map(renderSlot).toArray()}
                </tbody>
            </table>
        </div>
    )
}

export default Equipped
