import React from 'react'
import { Col, Row } from 'react-bootstrap'

const CreatureCombatPowerSummary = ({combatPower, creature}) => (
    <div>
        <table className="table table-condensed table-striped table-hover">
            <thead>
                <tr>
                    <th>Attacking against</th>
                    <th style={{width: 100}}>Power</th>
                </tr>
            </thead>
            <tbody>
                {combatPower.opposed
                    .get(creature)
                    .sortBy((_, key) => key, (a, b) => a < b ? -1 : 1)
                    .map((power, c) => (
                        <tr key={c}>
                            <td>{c}</td>
                            <td>{power}</td>
                        </tr>
                    ))
                    .toArray()
                }
            </tbody>
        </table>
        <table className="table table-condensed table-striped table-hover">
            <thead>
                <tr>
                    <th>Attacked by</th>
                    <th style={{width: 100}}>Power</th>
                </tr>
            </thead>
            <tbody>
                {combatPower.opposed
                    .filter((_, c) => c !== creature)
                    .map(opponents => opponents.find((_, c) => c === creature))
                    .sortBy((_, key) => key, (a, b) => a < b ? -1 : 1)
                    .map((power, c) => (
                        <tr key={c}>
                            <td>{c}</td>
                            <td>{power}</td>
                        </tr>
                    ))
                    .toArray()
                }
            </tbody>
        </table>
    </div>
)

const CombatPowerSummary = ({combatPower, creature, selectCreature}) => (
    <div>
        <table className="table table-condensed table-striped table-hover">
            <thead>
                <tr>
                    <th>Creature</th>
                    <th style={{width: 100}}>Avg. Power</th>
                </tr>
            </thead>
            <tbody>
                {combatPower.average
                    .sortBy((_, key) => key, (a, b) => a < b ? -1 : 1)
                    .map((power, c) => (
                        <tr
                            key={c}
                            className={c === creature ? 'info' : null}
                            onClick={() => selectCreature(c === creature ? null : c)} >
                            <td>{c}</td>
                            <td>{power}</td>
                        </tr>
                    ))
                    .toArray()
                }
            </tbody>
        </table>
        {creature ? <CreatureCombatPowerSummary combatPower={combatPower} creature={creature} /> : null}
    </div>
)

const TemplateSummary = ({templates}) => (
    <table className="table table-condensed table-striped table-hover">
        <thead>
            <tr>
                <th>Template</th>
                <th style={{width: 100}}>Success %</th>
            </tr>
        </thead>
        <tbody>
            {templates
                .sortBy((_, key) => key, (a, b) => a < b ? -1 : 1)
                .map((percentage, template) => (
                    <tr key={template}>
                        <td>{template}</td>
                        <td>{percentage}</td>
                    </tr>
                ))
                .toArray()
            }
        </tbody>
    </table>
)

const Summary = ({creature, summary, selectCreature}) => {
    if (! summary.valid) {
        return <h2 className="text-danger">Invalid data</h2>
    }

    return <Row>
        <Col md={6}>
            <h4>Combat Power</h4>
            <CombatPowerSummary combatPower={summary.combatPower} creature={creature} selectCreature={selectCreature} />
        </Col>
        <Col md={6}>
            <h4>Templates</h4>
            <TemplateSummary templates={summary.templates} />
        </Col>
    </Row>
}

export default Summary
