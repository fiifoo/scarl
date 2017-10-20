import { List, Map } from 'immutable'

export const isPolymorphic = model => model.polymorphic.length > 0

export const readItemId = (model, item) => isPolymorphic(model) ? item.getIn(['data', 'id']) : item.get('id')

export const createItem = (model, id, models) => (
    isPolymorphic(model) ? (
        Map({
            type: null,
            data: Map({id})
        })
    ) : (
        getNewModelValue(model, models).set('id', id)
    )
)

export const getNewValue = (fieldType, models) => {
    switch (fieldType.type) {
        case 'FormField': {
            const model = models.sub.get(fieldType.data.model, models.main.get(fieldType.data.model))

            if (isPolymorphic(model)) {
                return Map()
            } else {
                return getNewModelValue(model, models)
            }
        }
        case 'BooleanField': {
            return false
        }
        case 'DecimalField':
        case 'IntegerField': {
            return 0
        }
        case 'ListField':
        case 'MapField': {
            return List()
        }
        case 'PolymorphicRelationField': {
            return Map()
        }
        default: {
            return null
        }
    }
}

const getNewModelValue = (model, models) => Map(model.properties.map(property => [
    property.name,
    property.fieldType.data.required ? getNewValue(property.fieldType, models) : null,
]))
