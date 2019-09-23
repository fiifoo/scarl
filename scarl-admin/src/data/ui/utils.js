import { is, Map, Record } from 'immutable'
import Main from './Main'
import Form from './Form'

export const readUi = raw => ({
    main: Main.read(raw.main),
    form: Record({
        tab: raw.form.tab,
        tabs: Map(raw.form.tabs).map(Form.read)
    })()
})

export const writeUi = ui => ({
    main: Main.write(ui.main),
    form: {
        tab: ui.form.tab,
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
