import { isPolymorphic } from '../../data/utils.js'
import FixedContentField from '../area/FixedContentField.jsx'
import FixedTemplateFieldContainer from '../area/FixedTemplateFieldContainer.jsx'
import MachinerySourceField from '../area/MachinerySourceField.jsx'
import FactionField from '../faction/FactionField.jsx'
import RegionField from '../world/RegionField.jsx'
import RegionVariantKeyField from '../world/RegionVariantKeyField.jsx'
import WorldField from '../world/WorldField.jsx'
import BooleanField from './BooleanField.jsx'
import CharField from './CharField.jsx'
import ColorField from './ColorField.jsx'
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
import TagField from './TagField.jsx'
import TextField from './TextField.jsx'

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

const aliasFieldComponents = {
    Color: ColorField,
    Tag: TagField,
    Text: TextField,
}

const modelFieldComponents = {
    Faction: FactionField,
    FixedContent: FixedContentField,
    FixedTemplate: FixedTemplateFieldContainer,
    'FixedContent.MachinerySource': MachinerySourceField,

    Region: RegionField,
    World: WorldField,
    'Site.RegionVariantKey': RegionVariantKeyField,

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
    if (allowCustom && fieldType.data.alias && aliasFieldComponents[fieldType.data.alias]) {
        return aliasFieldComponents[fieldType.data.alias]
    }

    if (model && allowCustom && modelFieldComponents[model.id]) {
        return modelFieldComponents[model.id]
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
