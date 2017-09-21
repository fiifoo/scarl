import { Map } from 'immutable'

export const isPolymorphic = model => model.polymorphic.length > 0

export const readItemId = (model, item) => isPolymorphic(model) ? item.getIn(['data', 'id']) : item.get('id')

export const createItem = (model, id) => (
    isPolymorphic(model) ? (
        Map({
            type: null,
            data: Map({id})
        })
    ) : (
        Map({id})
    )
)
