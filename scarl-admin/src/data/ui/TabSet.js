import { Record, Set } from 'immutable'

const TabSet = Record({
    id: undefined,
    name: null,
    visible: true,
    tabs: Set(),
})
TabSet.read = ({tabs, ...raw}) => TabSet({
    ...raw,
    tabs: Set(tabs),
})
TabSet.write = tabSet => tabSet.toJS()

export default TabSet
