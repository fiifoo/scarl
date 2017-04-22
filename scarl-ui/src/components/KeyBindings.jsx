import { Map } from 'immutable'
import React from 'react'
import { Modal, Tab, Tabs } from 'react-bootstrap'
import * as modes from '../game/modes'
import * as keycodes from '../keyboard/keycodes'
import bindings from '../keyboard/bindings'

const keycodeNames = Map(keycodes).flip()

const humanize = value => value[0].toUpperCase() + (value.slice(1).toLowerCase().split('_').join(' '))

const sortMapper = (command, keycode) => keycode
const sorter = (a, b) => keycodeNames.get(a) < keycodeNames.get(b) ? -1 : 1

const Bindings = ({bindings}) => {

    const renderBinding = (command, keycode) => (
        <tr>
            <td>{humanize(keycodeNames.get(keycode))}</td>
            <td>{humanize(command)}</td>
        </tr>
    )

    return (
        <table className="table table-condensed table-striped" style={{marginTop: '1em'}}>
            <tbody>
                {bindings.sortBy(sortMapper, sorter).map(renderBinding)}
            </tbody>
        </table>
    )
}

const KeyBindings = ({visible, focusKeyboard, toggle}) =>  {

    const renderMode = mode => (
        <Tab key={mode} eventKey={mode} title={humanize(mode)}>
            <Bindings bindings={bindings[mode]} />
        </Tab>
    )

    return (
        <Modal show={visible} onHide={toggle} onExited={focusKeyboard}>
            <Modal.Header closeButton>
                <Modal.Title>Key bindings</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Tabs id="key-bindings-tabs" bsStyle="pills">
                    {Map(modes).map(renderMode)}
                </Tabs>
            </Modal.Body>
        </Modal>
    )
}

export default KeyBindings
