import React from 'react'
import { Col, Row } from 'react-bootstrap'

const CreatureSummary = ({combatPower, creature}) => (
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

const AverageSummary = ({combatPower, creature, selectCreature}) => (
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
)

const CombatPowerSummary = ({combatPower, creature, selectCreature}) => (
    <Row>
        <Col md={6}>
            <AverageSummary combatPower={combatPower} creature={creature} selectCreature={selectCreature} />
        </Col>
        <Col md={6}>
            {creature ? <CreatureSummary combatPower={combatPower} creature={creature} /> : null}
        </Col>
    </Row>
)

export default CombatPowerSummary
