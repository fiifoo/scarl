import { is, Map, Record } from 'immutable'
import AreaEditor from './AreaEditor'
import Main from './Main'
import Form from './Form'

export const readUi = raw => ({
    main: Main.read(raw.main),

    areaEditor: Record({
        tab: raw.main.tab,
        tabs: Map(raw.areaEditor.tabs).map(AreaEditor.read)
    })(),
    form: Record({
        tab: raw.main.tab,
        tabs: Map(raw.form.tabs).map(Form.read)
    })(),
})

export const writeUi = ui => ({
    main: Main.write(ui.main),

    areaEditor: {
        tabs: ui.areaEditor.tabs.map(AreaEditor.write).map((v, k) => [k, v]).toArray()
    },
    form: {
        tabs: ui.form.tabs.map(Form.write).map((v, k) => [k, v]).toArray()
    },
})

export const shouldSaveUi = state => (
    ! state.initialUi || (
        ! is(state.initialUi.main.tabs, state.ui.main.tabs)
    ) || (
        ! is(state.initialUi.main.tabSets, state.ui.main.tabSets)
    )
)
