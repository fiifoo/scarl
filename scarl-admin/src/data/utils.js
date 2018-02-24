import { List, Map } from 'immutable'

export const isPolymorphic = model => model.polymorphic.length > 0

export const readItemId = (model, item) => isPolymorphic(model) ? item.getIn(['data', 'id']) : item.get('id')

export const writeItemId = (model, item, id) => isPolymorphic(model) ? item.setIn(['data', 'id'], id) : item.set('id', id)

export const isNewItemId = (data, model, id) => !data.hasIn(model.dataPath.concat([id]))

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

export const copyItem = (model, sourceId, targetId, data) => {
    const source = data.getIn(model.dataPath.concat([sourceId]))

    return writeItemId(model, source, targetId)
}

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

export const sanitize = (data, models) => {
    const reducer = ({data}, {path, fieldType}) => {
        const value = data.getIn(path)

        data = value !== undefined || ! fieldType.data.required ? data : data.setIn(
            path,
            getNewValue(fieldType, models)
        )

        return {data}
    }

    return reduceModels(models, reducer, {data}).data
}

export const getItemReferences = (data, models) => (model, id) => {
    const reducer = ({data, references}, {path, fieldType}) => {
        switch (fieldType.type) {
            case 'RelationField': {
                if (fieldType.data.model === model.id) {
                    const value = data.getIn(path)

                    if (value === id) {
                        references = references.push(path)
                    }
                }
                break
            }
            case 'PolymorphicRelationField': {
                const value = data.getIn(path)

                if (value.get('type') === model.id && value.get('value') === id) {
                    references = references.push(path.concat(['value']))
                }
                break
            }
        }

        return {data, references}
    }

    return reduceModels(models, reducer, {data, references: List()}).references
}

const getNewModelValue = (model, models) => Map(model.properties.map(property => [
    property.name,
    property.fieldType.data.required ? getNewValue(property.fieldType, models) : null,
]))

const reduceModels = (models, reducer, initial) => {

    const reduceMainModel = (carry, model) => (
        carry.data.getIn(model.dataPath).reduce(reduceItem(model), carry)
    )

    const reduceItem = model => (carry, item) => {
        const path = model.dataPath.concat([readItemId(model, item)])

        return reduceModel(path)(carry, model)
    }

    const reduceModel = path => (carry, model) => {
        if (isPolymorphic(model)) {
            const type = carry.data.getIn(path.concat(['type']))

            if (type) {
                return models.sub.get(type).properties.reduce(reduceProperty(path.concat(['data'])), carry)
            } else {
                return carry
            }
        } else {
            return model.properties.reduce(reduceProperty(path), carry)
        }
    }

    const reduceProperty = modelPath => (carry, property) => {
        const path = modelPath.concat([property.name])
        const fieldType = property.fieldType

        return reduceField(path)(carry, fieldType)
    }

    const reduceField = path => (carry, fieldType) => {
        carry = reducer(carry, {path, fieldType})

        const value = carry.data.getIn(path)

        if (value == null) {
            return carry
        }

        switch (fieldType.type) {
            case 'FormField': {
                const model = models.sub.get(fieldType.data.model)

                return reduceModel(path)(carry, model)
            }
            case 'ListField': {
                return value.reduce(
                    (carry, _, key) => {
                        return reduceField(path.concat([key]))(carry, fieldType.data.value)
                    },
                    carry
                )
            }
            case 'MapField': {
                return value.reduce(
                    (carry, _, key) => {
                        carry = reduceField(path.concat([key, 0]))(carry, fieldType.data.key)
                        carry = reduceField(path.concat([key, 1]))(carry, fieldType.data.value)

                        return carry
                    },
                    carry
                )
            }
            default: {
                return carry
            }
        }
    }

    return models.main.reduce(reduceMainModel, initial)
}
