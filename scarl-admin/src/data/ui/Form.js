import { List, Record } from 'immutable'

export const ItemReferences = Record({
    references: List(),
    model: null,
    id: null,
})

export const ItemAdd = Record({
    id: null,
    invalid: false,
})

export const ItemRename = Record({
    id: null,
    invalid: false,
})

const Form = Record({
    item: null,
    itemAdd: ItemAdd(),
    itemReferences: null,
    itemRename: ItemRename(),
    model: null,
    sideForm: null,
})
Form.read = raw => Form({
    item: raw.item,
    model: raw.model,
})
Form.write = form => ({
    item: form.item,
    model: form.model,
})

export default Form
