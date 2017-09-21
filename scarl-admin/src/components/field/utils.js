import BooleanField from './BooleanField.jsx'
import CharField from './CharField.jsx'
import DecimalField from './DecimalField.jsx'
import FormField from './FormField.jsx'
import IntegerField from './IntegerField.jsx'
import ListField from './ListField.jsx'
import MapField from './MapField.jsx'
import PolymorphicRelationField from './PolymorphicRelationField.jsx'
import RelationField from './RelationField.jsx'
import StringField from './StringField.jsx'

const fieldComponents = {
    BooleanField,
    CharField,
    DecimalField,
    FormField,
    IntegerField,
    ListField,
    MapField,
    PolymorphicRelationField,
    RelationField,
    StringField,
}

export const getFieldComponent = fieldType => {
    const component = fieldComponents[fieldType.type]

    if (! component) {
        throw new Error(`Unknown field type "${fieldType.type}".`)
    }

    return component
}

export const getFieldModel = (fieldType, models) => (
    fieldType.type === 'FormField' ? models.sub.get(fieldType.data.model) : null
)
