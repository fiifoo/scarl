import React from 'react'
import Models from '../../data/Models'
import BooleanSelectRow from '../form/BooleanSelectRow.jsx'
import SelectRow from '../form/SelectRow.jsx'
import TextInputRow from '../form/TextInputRow.jsx'
import EditorLocationField from './EditorLocationField.jsx'

const EditorLocation = ({common, machinery, content, setContent}) => {
    const {data, models} = common

    const setValue = property => value => setContent(content.set(property, value))

    return (
        <div>
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
            <EditorLocationField
                property="machineryControls"
                value={content.machineryControls}
                setValue={setValue('machineryControls')}
                machinery={machinery}
                common={common} />
            <EditorLocationField
                property="machineryTargets"
                value={content.machineryTargets}
                setValue={setValue('machineryTargets')}
                machinery={machinery}
                common={common} />
            <EditorLocationField
                property="creature"
                value={content.creature}
                setValue={setValue('creature')}
                common={common} />
            <EditorLocationField
                property="items"
                value={content.items}
                setValue={setValue('items')}
                common={common} />
            <EditorLocationField
                property="terrain"
                value={content.terrain}
                setValue={setValue('terrain')}
                common={common} />
            <EditorLocationField
                property="wall"
                value={content.wall}
                setValue={setValue('wall')}
                common={common} />
            <EditorLocationField
                property="widget"
                value={content.widget}
                setValue={setValue('widget')}
                common={common} />
        </div>
    )
}

export default EditorLocation
