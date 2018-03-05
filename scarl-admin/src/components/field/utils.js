import { isPolymorphic } from '../../data/utils.js'
import FixedContentField from '../area/FixedContentField.jsx'
import FixedTemplateField from '../area/FixedTemplateField.jsx'
import MachinerySourceField from '../area/MachinerySourceField.jsx'
import BooleanField from './BooleanField.jsx'
import CharField from './CharField.jsx'
import DecimalField from './DecimalField.jsx'
import FormField from './FormField.jsx'
import IntegerField from './IntegerField.jsx'
import ListField from './ListField.jsx'
import MapField from './MapField.jsx'
import PolymorphicFormField from './PolymorphicFormField.jsx'
import PolymorphicObjectField from './PolymorphicObjectField.jsx'
import PolymorphicRelationField from './PolymorphicRelationField.jsx'
import RelationField from './RelationField.jsx'
import SideFormField from './SideFormField.jsx'
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

const customFieldComponents = {
    FixedContent: FixedContentField,
    FixedTemplate: FixedTemplateField,
    'FixedContent.MachinerySource': MachinerySourceField,
    Stats: SideFormField,
}

export const createFormFieldType = model => ({
    type: 'FormField',
    data: {
        model: model.id,
        required: true,
    }
})

export const getFieldComponent = (fieldType, model = null, allowCustom = true) => {
    if (model && allowCustom && customFieldComponents[model.id]) {
        return customFieldComponents[model.id]
    }

    const component = fieldComponents[fieldType.type]

    if (! component) {
        throw new Error(`Unknown field type "${fieldType.type}".`)
    }

    return component === FormField && isPolymorphic(model) ? (
        model.objectPolymorphism ? (
            PolymorphicObjectField
        ) : (
            PolymorphicFormField
        )
    ) : (
        component
    )
}

export const getFieldModel = (fieldType, models) => (
    fieldType.type === 'FormField' ? models.sub.get(fieldType.data.model) : null
)
