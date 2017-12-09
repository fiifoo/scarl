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

export const sanitize = (data, models) => (
    models.main.reduce(
        (data, model) => data.getIn(model.dataPath).reduce(
            sanitizeItem(models, model),
            data
        ),
        data
    )
)

const sanitizeItem = (models, model) => {
    const pass = sanitizeModel(models, model)

    return (
        (data, item) => pass(data, model.dataPath.concat([readItemId(model, item)]))
    )
}

const sanitizeModel = (models, model) => (data, path) => (
    // removing extraneous values not implemented

    model.properties.reduce(
        sanitizeProperty(models, path),
        data
    )
)

const sanitizeProperty = (models, path) => (data, property) => {
    const fieldType = property.fieldType
    const propertyPath = path.concat([property.name])
    const value = data.getIn(propertyPath)

    const next = value !== undefined || ! fieldType.data.required ? data : data.setIn(
        propertyPath,
        getNewValue(fieldType, models)
    )

    if (value != null || fieldType.data.required) {
        switch (fieldType.type) {
            case 'FormField': {
                const model = models.sub.get(fieldType.data.model)

                return sanitizeModel(models, model)(next, propertyPath)
            }
            case 'ListField':
            case 'MapField': { // map field untested & key not supported
                if (fieldType.data.value.type === 'FormField') {
                    return value.reduce(
                        (next, _, key) => {
                            const model = models.sub.get(fieldType.data.value.data.model)

                            return sanitizeModel(models, model)(next, propertyPath.concat([key]))
                        },
                        next
                    )
                }
            }
        }
    }

    return next
}

const getNewModelValue = (model, models) => Map(model.properties.map(property => [
    property.name,
    property.fieldType.data.required ? getNewValue(property.fieldType, models) : null,
]))
