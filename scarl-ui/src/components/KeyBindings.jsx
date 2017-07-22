import { Map } from 'immutable'
import React from 'react'
import { Tab, Tabs } from 'react-bootstrap'
import * as modes from '../game/modes'
import * as keycodes from '../keyboard/keycodes'
import bindings from '../keyboard/bindings'

const keycodeNames = Map(keycodes).flip()

const humanize = value => value[0].toUpperCase() + (value.slice(1).toLowerCase().split('_').join(' '))

const sortMapper = (command, keycode) => keycode
const sorter = (a, b) => keycodeNames.get(a) < keycodeNames.get(b) ? -1 : 1

const Bindings = ({bindings}) => {

    const renderBinding = (command, keycode) => (
        <tr key={keycode}>
            <td>{humanize(keycodeNames.get(keycode))}</td>
            <td>{humanize(command)}</td>
        </tr>
    )

    return (
        <table className="table table-condensed table-striped" style={{marginTop: '1em'}}>
            <tbody>
                {bindings.sortBy(sortMapper, sorter).map(renderBinding).toArray()}
            </tbody>
        </table>
    )
}

const KeyBindings = () =>  {

    const renderMode = mode => (
        <Tab key={mode} eventKey={mode} title={humanize(mode)}>
            <Bindings bindings={bindings[mode]} />
        </Tab>
    )

    return (
        <Tabs id="key-bindings-tabs" bsStyle="pills">
            {Map(modes)
                .filterNot(mode => bindings[mode].isEmpty())
                .map(renderMode)
                .toArray()
            }
        </Tabs>
    )
}

export default KeyBindings
