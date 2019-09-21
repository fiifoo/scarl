import { Record, Set } from 'immutable'

const TabSet = Record({
    id: undefined,
    name: null,
    visible: true,
    tabs: Set(),
})

export default TabSet
