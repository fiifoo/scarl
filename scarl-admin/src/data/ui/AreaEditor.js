import { Record, Set } from 'immutable'
import Location from '../../data/area/Location'

const defaultLocation = Location({x: 0, y: 0})

const AreaEditor = Record({
    visible: false,
    location: defaultLocation,
    locations: Set([defaultLocation]),
    brush: Record({
        property: null,
        value: null,
    })(),
})
AreaEditor.read = raw => AreaEditor({
    visible: raw.visible,
})
AreaEditor.write = areaEditor => ({
    visible: areaEditor.visible,
})

export default AreaEditor
