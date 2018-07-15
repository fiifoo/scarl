import { is, List, Map, Range, Record } from 'immutable'
import { calculateDimensions } from './Shape'
import Location from './Location'

const LocationContent = Record({
    conduit: undefined,
    creature: null,
    entrance: null,
    gateway: false,
    items: List(),
    template: null,
    templateLocation: false,
    terrain: null,
    wall: null,
    widget: null,
    machineryControls: List(),
    machineryTargets: List(),
})

const empty = LocationContent()

const isEmptyValue = (property, value) => property === 'conduit' ? (
    value === undefined
) : (
    ! value || (value instanceof List && value.size === 0)
)

const readMap = (contents, template, path, property) => (
    template.getIn(path).reduce((contents, x) => {
        const location = Location(x.first())
        const value = x.last()

        return contents.set(
            location,
            contents.get(location, empty).set(property, value)
        )
    }, contents)
)

const readSet = (contents, template, path, property) => (
    template.getIn(path).reduce((contents, l) => {
        const location = Location(l)

        return contents.set(
            location,
            contents.get(location, empty).set(property, true)
        )
    }, contents)
)

const readSingleMachinery = (machineryProperty, property) => (contents, machinery, key) => (
    machinery.get(machineryProperty).reduce((contents, l) => {
        const location = Location(l)
        const prev = contents.get(location, empty)
        const next = prev.set(property, prev.get(property).push(key))

        return contents.set(
            location,
            next
        )
    }, contents)
)

const readMachinery = machineryProperty => (contents, template, path, property) => (
    template.getIn(path).reduce(readSingleMachinery(machineryProperty, property), contents)
)

const readTemplateLocation = (contents, template, path, property, data) => (
    template.getIn(path).reduce((contents, x) => {
        const corner = Location(x.first())
        const value = x.last()
        const shape = data.templates.getIn([value, 'data', 'shape'])
        const dimensions = calculateDimensions(shape)
        const width = dimensions.outerWidth
        const height = dimensions.outerHeight

        return Range(0, width).reduce((contents, x) => Range(0, height).reduce((contents, y) => {
            const location = Location.add(corner, Location({x, y}))

            return contents.set(
                location,
                contents.get(location, empty).set(property, true)
            )
        }, contents), contents)
    }, contents)
)

const writeMap = (template, location, content, path, property) => {
    const current = template.getIn(path)
    const cleared = current.filter(x => ! is(x.first(), location))
    const next = isEmptyValue(property, content.get(property)) ? (
        cleared
    )  : (
        cleared.push(List([location, content.get(property)]))
    )

    return template.setIn(path, next)
}

const writeSet = (template, location, content, path, property) => {
    const current = template.getIn(path)
    const cleared = current.filter(l => ! is(l, location))
    const next = content.get(property) ? (
        cleared.push(location)
    ) : (
        cleared
    )

    return template.setIn(path, next)
}

const writeMachinery = machineryProperty => (template, location, content, path, property) => {
    const current = template.getIn(path)
    const cleared = current.map(machinery => (
        machinery.set(
            machineryProperty,
            machinery.get(machineryProperty).filter(l => ! is(l, location))
        )
    ))
    const next = content.get(property).reduce((list, key) => {
        const current = list.get(key)
        const next = current.set(
            machineryProperty,
            current.get(machineryProperty).push(location)
        )

        return list.set(key, next)
    }, cleared)

    return template.setIn(path, next)
}

const paths = {
    conduit: ['content', 'conduitLocations'],
    creature: ['content', 'creatures'],
    entrance: ['entrances'],
    gateway: ['content', 'gatewayLocations'],
    items: ['content', 'items'],
    template: ['templates'],
    templateLocation: ['templates'],
    terrain: ['content', 'terrains'],
    wall: ['content', 'walls'],
    widget: ['content', 'widgets'],
    machineryControls: ['content', 'machinery'],
    machineryTargets: ['content', 'machinery'],
}

const readers = Map({
    conduit: readMap,
    creature: readMap,
    entrance: readMap,
    gateway: readSet,
    items: readMap,
    template: readMap,
    templateLocation: readTemplateLocation,
    terrain: readMap,
    wall: readMap,
    widget: readMap,
    machineryControls: readMachinery('controls'),
    machineryTargets: readMachinery('targets'),
})

const writers = Map({
    conduit: writeMap,
    creature: writeMap,
    entrance: writeMap,
    gateway: writeSet,
    items: writeMap,
    template: writeMap,
    templateLocation: t => t,
    terrain: writeMap,
    wall: writeMap,
    widget: writeMap,
    machineryControls: writeMachinery('controls'),
    machineryTargets: writeMachinery('targets'),
})

LocationContent.read = (template, data) => readers.reduce((contents, reader, property) => {
    const path = paths[property]

    return reader(contents, template, path, property, data)
}, Map())


LocationContent.write = (template, location, content) => writers.reduce((template, writer, property) => {
    const path = paths[property]

    return writer(template, location, content, path, property)
}, template)

LocationContent.merge = (source, target) => (
    LocationContent({
        conduit: source.conduit || target.conduit,
        creature: source.creature || target.creature,
        entrance: source.entrance || target.entrance,
        gateway: source.gateway || target.gateway,
        items: target.items.concat(source.items),
        template: source.template || target.template,
        terrain: source.terrain || target.terrain,
        wall: source.wall || target.wall,
        widget: source.widget || target.widget,
        machineryControls: target.machineryControls.concat(source.machineryControls),
        machineryTargets: target.machineryTargets.concat(source.machineryTargets),
    })
)

export default LocationContent
