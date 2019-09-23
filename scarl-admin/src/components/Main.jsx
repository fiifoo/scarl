import React from 'react'
import { Col, Row } from 'react-bootstrap'
import {SortableContainer, SortableElement} from 'react-sortable-hoc'
import ItemAddContainer from '../containers/ItemAddContainer'
import ItemFormContainer from '../containers/ItemFormContainer'
import ItemReferencesContainer from '../containers/ItemReferencesContainer'
import ItemSelectContainer from '../containers/ItemSelectContainer'
import ModelSelectContainer from '../containers/ModelSelectContainer'
import FormRow from './form/FormRow.jsx'

const AddTab = ({tabSet, addTab}) => (
    <div className="main-tab">
        <div className="main-tab-close" />
        <div className="main-tab-label" onClick={() => addTab(tabSet.id)}>
            &#10133;
        </div>
    </div>
)

const Tab = SortableElement(({active = false, label, select, remove = undefined}) => {
    return (
        <div className={active ? 'main-tab active' : 'main-tab'}>
            <div className="main-tab-close">
                <button
                    type="button"
                    className="btn btn-xs btn-link"
                    disabled={! remove}
                    onClick={remove}>
                    &#10060;
                </button>
            </div>
            <div className="main-tab-label" onClick={select}>
                {label}
            </div>
        </div>
    )
})

const TabLabel = ({tab, contents}) => {
    const model = contents.get('model')
    const item = contents.get('item')

    return model ? (
        <div>
            <div style={{marginBottom: 3}}><i className="text-muted">{model}</i></div>
            <div>{item || '---'}</div>
        </div>
    ) : (
        `Tab ${tab}`
    )
}

const Tabs = SortableContainer(props => {
    const {activeTab, tabContents, tabs, tabSet} = props
    const {addTab, changeTab, deleteTab} = props

    const renderTab = ({tab, index}) => {
        const label = (
            <TabLabel
                tab={tab}
                contents={tabContents.get(tab)} />
        )
        const remove = tabs.size > 1 ? () => deleteTab(tab) : null

        return (
            <Tab
                key={tab}
                index={index}
                active={tab === activeTab}
                label={label}
                select={() => changeTab(tab)}
                remove={remove} />
        )
    }

    return (
        <div>
            {tabs.map((tab, index) => ({
                tab,
                index,
            })).filter(x => tabSet.tabs.contains(x.tab)).map(renderTab)}
            <AddTab tabSet={tabSet} addTab={addTab} />
        </div>
    )
})

const TabSet = SortableElement(({tabSet, sortTabs, remove, rename, toggle, ...props}) => {
    return (
        <div>
            <div className="main-tab main-tabset" onClick={toggle}>
                <div className="main-tab-close">
                    <button
                        type="button"
                        className="btn btn-xs btn-link"
                        disabled={! remove}
                        onClick={remove}>
                        &#10060;
                    </button>
                </div>
                <div className="main-tab-label">
                    <input
                        type="text"
                        value={tabSet.name !== null ? tabSet.name : `Tabs ${tabSet.id}`}
                        onChange={e => rename(e.target.value)}
                        onClick={e => e.stopPropagation()} />
                </div>
            </div>
            {tabSet.visible && (
                <Tabs
                    distance={10}
                    onSortEnd={sortTabs}

                    tabSet={tabSet}
                    {...props} />
            )}
        </div>
    )
})

const AddTabSet = ({addTabSet, readonly, shouldSaveUi, saveUi}) => (
    <div className="main-tab main-tabset add">
        <div className="main-tab-close">
            <button
                type="button"
                className={shouldSaveUi && ! readonly ? 'btn btn-xs btn-primary' : 'btn btn-xs btn-default'}
                disabled={readonly}
                onClick={saveUi}>
                &#128427; Save
            </button>
        </div>
        <div className="main-tab-label" onClick={addTabSet}>
            &#10133;
        </div>
    </div>
)

const TabSets = SortableContainer(({tabSets, addTabSet, readonly, shouldSaveUi, deleteTabSet, renameTabSet, saveUi, toggleTabSet, ...props}) => {
    const renderTabSet = (tabSet, index) => {
        const remove = tabSets.find(x => x !== tabSet && x.tabs.size > 0) !== undefined ? () => deleteTabSet(tabSet) : null
        const rename = name => renameTabSet(tabSet, name)
        const toggle = () => toggleTabSet(tabSet)

        return (
            <TabSet
                key={tabSet.id}
                index={index}
                tabSet={tabSet}
                remove={remove}
                rename={rename}
                toggle={toggle}
                {...props} />
        )
    }

    return (
        <div>
            <AddTabSet addTabSet={addTabSet} readonly={readonly} shouldSaveUi={shouldSaveUi} saveUi={saveUi} />
            {tabSets.map(renderTabSet)}
        </div>
    )
})

const Content = ({model}) => (
    <div>
        <ItemReferencesContainer />
        <Selects model={model} />
        <ItemFormContainer />
    </div>
)

const Selects = ({model}) => (
    <div className="item-form-header form-horizontal">
        <FormRow label="Select model">
            <ModelSelectContainer />
        </FormRow>
        { model && (
            <FormRow label="Select item">
                <Row>
                    <Col sm={6}><ItemSelectContainer /></Col>
                    <Col sm={6}><ItemAddContainer /></Col>
                </Row>
            </FormRow>
        )}
    </div>
)

const Main = ({model, tab, sortTabSets, ...props}) => {
    return (
        <Row>
            <Col sm={3}>
                <TabSets
                    distance={10}
                    onSortEnd={sortTabSets}

                    activeTab={tab}
                    {...props} />
            </Col>
            <Col sm={9}>
                <Content key={tab} model={model} />
            </Col>
        </Row>
    )
}


export default Main
