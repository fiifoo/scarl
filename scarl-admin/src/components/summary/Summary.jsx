import React from 'react'
import { Tab, Tabs } from 'react-bootstrap'
import * as tabs from '../../const/summaryTabs'
import CombatPowerSummary from './CombatPowerSummary.jsx'
import EquipmentCombatPowerSummary from './EquipmentCombatPowerSummary.jsx'
import TemplateSummary from './TemplateSummary.jsx'

const Summary = ({creature, summary, tab, changeTab, selectCreature}) => {
    if (! summary.valid) {
        return <h2 className="text-danger">Invalid data</h2>
    }

    return (
        <Tabs id="summary-tabs" activeKey={tab} onSelect={changeTab}>
            <Tab eventKey={tabs.TEMPLATES} title="Templates">
                <TemplateSummary templates={summary.templates} />
            </Tab>
            <Tab eventKey={tabs.COMBAT_POWER} title="Combat power">
                <CombatPowerSummary combatPower={summary.combatPower} creature={creature} selectCreature={selectCreature} />
            </Tab>
            <Tab eventKey={tabs.EQUIPMENT_COMBAT_POWER} title="Equipment combat power">
                <EquipmentCombatPowerSummary equipmentCombatPower={summary.combatPower.equipment} />
            </Tab>

        </Tabs>
    )
}

export default Summary
