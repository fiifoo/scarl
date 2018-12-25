import { Map } from 'immutable'
import React from 'react'
import SelectRow from '../form/SelectRow.jsx'

const RegionVariantKeyField = ({label, required, path, value, common}) => {
    const {horizontal, data, setValue} = common

    const siteId = path[1] // ....
    const regionId = data.getIn(['sites', siteId, 'region'])
    const variantKeys = data.getIn(['regions', regionId, 'variants']).map(x => x.getIn(['key', 'value']))

    const choices = variantKeys.map(key => ({
        value: key,
        label: key,
    })).toArray()

    const onChange = value => setValue(path, Map({value}))

    return (
        <SelectRow
            horizontal={horizontal}
            label={label}
            required={required}
            value={value ? value.get('value') : value}
            onChange={onChange}
            choices={choices} />
    )
}

export default RegionVariantKeyField
