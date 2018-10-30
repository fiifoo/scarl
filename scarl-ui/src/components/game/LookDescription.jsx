import React from 'react'
import { getCreatureInfo, getLocationCreatures, getLocationKinds, getLocationSummary } from '../../game/utils.js'

const Empty = () => <div><i className="text-muted">Nothing there</i></div>

const getLocationDescribables = (location, fov, map, kinds) => {
    const content = getLocationKinds(location, fov, map)

    if (content === undefined) {
        return []
    }

    const items = content.items.map(item => kinds.items.get(item))
    const walls = content.wall ? [kinds.walls.get(content.wall)] : []
    const terrains = content.terrain ? [kinds.terrains.get(content.terrain)] : []

    return items.concat(walls).concat(terrains)
}

const Dropdown = ({children, detailed, setLookDetailed}) => {
    return (
        <div className="screen-dropdown" style={{minWidth: 200}}>
            <div
                className="pull-right"
                onClick={() => setLookDetailed(! detailed)}>
                {detailed ? '▲' : '▼'}
            </div>
            <div style={{marginRight: 20}}>
                {children}
            </div>
        </div>
    )
}

const LookDescription = ({
    location, detailed, setLookDetailed,
    factions, fov, map, kinds, player,
}) => {

    if (! detailed) {
        const summary = getLocationSummary(factions, fov, map, kinds, player)(location)

        return (
            <Dropdown detailed={detailed} setLookDetailed={setLookDetailed}>
                {summary ? (
                    <div>{summary}</div>
                ) : (
                    <Empty />
                )}
            </Dropdown>
        )
    }

    const renderCreature = (creature, key) => {
        const kind = kinds.creatures.get(creature.kind)
        const info = getCreatureInfo(creature, player, factions)

        return (
            <div key={key}>
                <span>{kind.name}</span>
                {info && (
                    <span> ({info})</span>
                )}
                {kind.description && (
                    <span className="text-muted"> {kind.description}</span>
                )}
            </div>
        )
    }

    const renderKind = (kind, key) => (
        <div key={key}>
            <span>{kind.name}</span>
            {kind.description && (
                <span className="text-muted"> {kind.description}</span>
            )}
        </div>
    )

    const creatures = getLocationCreatures(location, fov).filter(x => x.id !== player.creature.id)
    const describables = getLocationDescribables(location, fov, map, kinds)

    const content = creatures.length > 0 || describables.length > 0 ? (
        <React.Fragment>
            {creatures.map(renderCreature)}
            {describables.map(renderKind)}
        </React.Fragment>
    ) : (
        <Empty />
    )

    return (
        <Dropdown detailed={detailed} setLookDetailed={setLookDetailed}>
            {content}
        </Dropdown>
    )
}

export default LookDescription
