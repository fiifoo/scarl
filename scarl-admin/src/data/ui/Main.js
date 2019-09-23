import {  List, Record, Set } from 'immutable'
import * as pages from '../../const/pages'
import TabSet from '../../data/ui/TabSet'

const Ui = Record({
    fetchingSummary: false,
    page: pages.MAIN,
    saving: false,
    simulating: false,
    tab: 1,
    tabs: List([1]),
    tabSets: List([TabSet({
        id: 1,
        tabs: Set([1])
    })]),
})
Ui.read = raw => Ui({
    tab: raw.tab,
    tabs: List(raw.tabs),
    tabSets: List(raw.tabSets.map(TabSet.read)),
})
Ui.write = ui => ({
    tab: ui.tab,
    tabs: ui.tabs.toArray(),
    tabSets: ui.tabSets.map(TabSet.write).toArray()
})

export default Ui
