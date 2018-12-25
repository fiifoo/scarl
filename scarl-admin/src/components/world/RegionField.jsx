import React from 'react'
import { createComponent } from '../utils.jsx'
import FormField from '../field/FormField.jsx'
import ListField from '../field/ListField.jsx'

const Conduits = props => {
    const {value, common} = props
    const {data, models} = common
    const region = value

    if (! region.get('world')) {
        return null
    }

    const path = ['worlds', region.get('world'), 'conduits']

    const conduits = data.getIn(path)
    const sites = data.get('sites').filter(x => x.get('region') === region.get('id')).keySeq().toSet()
    const validConduits = conduits.filter(conduit => (
        (! conduit.get('source') && ! conduit.get('target'))
        || sites.contains(conduit.get('source'))
        || sites.contains(conduit.get('target'))
    )).toSet()

    const model = models.main.get('World')
    const property = model.properties.find(x => x.name === 'conduits')

    return (
        <ListField
            name={property.name}
            label={property.name}
            required={true}
            model={null}
            fieldType={property.fieldType}
            path={path}
            value={conduits}
            common={common}
            filterValue={x => validConduits.contains(x)} />
    )
}

const RegionField = createComponent(FormField, {
    Extras: Conduits,
})

export default RegionField
