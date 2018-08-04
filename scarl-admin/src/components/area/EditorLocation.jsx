import { List } from 'immutable'
import React from 'react'
import BooleanSelectRow from '../form/BooleanSelectRow.jsx'
import SelectRow from '../form/SelectRow.jsx'
import TextInputRow from '../form/TextInputRow.jsx'
import Models from '../../data/Models'

const getMachineryChoices = machinery => machinery.map((machinery, key) => ({
    value: key,
    label: `${machinery.getIn(['mechanism', 'type'])} (${key})`,
})).toArray()

const EditorLocation = ({common, machinery, content, setContent}) => {
    const {data, models} = common

    const machineryChoices = getMachineryChoices(machinery)
    const setValue = property => value => setContent(content.set(property, value))
    const setMultiValue = property => value => setContent(content.set(property, List(value)))

    return (
        <div>
            <SelectRow
                label="Creature"
                choices={Models.choices(models, data, 'CreatureKind')}
                value={content.creature}
                onChange={setValue('creature')} />
            <SelectRow
                label="Items"
                choices={Models.choices(models, data, 'ItemKind')}
                value={content.items.toArray()}
                multi={true}
                onChange={setMultiValue('items')} />
            <SelectRow
                label="Terrain"
                choices={Models.choices(models, data, 'TerrainKind')}
                value={content.terrain}
                onChange={setValue('terrain')} />
            <SelectRow
                label="Wall"
                choices={Models.choices(models, data, 'WallKind')}
                value={content.wall}
                onChange={setValue('wall')} />
            <SelectRow
                label="Widget"
                choices={Models.choices(models, data, 'WidgetKind')}
                value={content.widget}
                onChange={setValue('widget')} />
            <SelectRow
                label="Entrance"
                choices={Models.choices(models, data, 'ItemKind')}
                value={content.entrance}
                onChange={setValue('entrance')} />
            <BooleanSelectRow
                label="Conduit"
                value={content.conduit !== undefined}
                onChange={value => value ? setValue('conduit')(null) : setValue('conduit')(undefined)} />
            <TextInputRow
                label="Conduit tag"
                disabled={content.conduit === undefined}
                value={content.conduit}
                onChange={setValue('conduit')} />
            <BooleanSelectRow
                label="Gateway"
                value={content.gateway}
                onChange={setValue('gateway')} />
            <BooleanSelectRow
                label="Restricted"
                value={content.restricted}
                onChange={setValue('restricted')} />
            <SelectRow
                label="Template"
                choices={Models.choices(models, data, 'Template')}
                value={content.template}
                onChange={setValue('template')} />
            <SelectRow
                label="Machinery controls"
                choices={machineryChoices}
                value={content.machineryControls.toArray()}
                multi={true}
                onChange={setMultiValue('machineryControls')} />
            <SelectRow
                label="Machinery targets"
                choices={machineryChoices}
                value={content.machineryTargets.toArray()}
                multi={true}
                onChange={setMultiValue('machineryTargets')} />
        </div>
    )
}

export default EditorLocation
