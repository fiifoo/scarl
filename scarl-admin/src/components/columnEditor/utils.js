import { List, Record } from 'immutable'
import { isPolymorphic } from '../../data/utils'

const ColumnEditorProperty = Record({
    model: undefined,
    fieldType: undefined,
    path: undefined,
})

export const getColumnEditorProperties = models => mainModel => {

    if (isPolymorphic(mainModel)) {
        return List()
    }

    const reduceModel = path => (carry, model) => (
        model.properties.reduce(reduceProperty(model, path), carry)
    )

    const reduceProperty = (model, parentPath) => (carry, property) => {
        const path = parentPath.push(property.name)
        const fieldType = property.fieldType

        const next = carry.push(ColumnEditorProperty({
            fieldType,
            path,
        }))

        switch (fieldType.type) {
            case 'ListField':
            case 'MapField': {
                return carry // could support these but won't because of layout issues
            }
            case 'FormField': {
                const subModel = models.sub.get(fieldType.data.model)

                if (subModel.id === model.id) {
                    return carry // can't support infinite recursion
                } else if (! isPolymorphic(subModel)) {
                    return reduceModel(path)(next, subModel)
                }
            }
        }

        return next
    }

    return reduceModel(List())(List(), mainModel)
}
