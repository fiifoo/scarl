import React from 'react'
import { Col, Row } from 'react-bootstrap'
import { isPolymorphic, readItemId } from '../data/utils'
import './field/utils' // needs to be imported before FormField for correct import order
import FormField from './field/FormField.jsx'
import ReadonlyRow from './form/ReadonlyRow.jsx'

const ItemForm = ({item, model, setItemValue, data, models}) => {
    const submit = event => {
        event.preventDefault()

        return false
    }

    const id = readItemId(model, item)
    const path = model.dataPath.concat([id])
    const label = isPolymorphic(model) ? 'type' : undefined

    return  (
        <form onSubmit={submit} className="form-horizontal item-form">
            <Row>
                <Col md={6}>
                    <ReadonlyRow label="id" value={id} />
                    <FormField
                        label={label}
                        required={true}
                        model={model}
                        path={path}
                        value={item}
                        setValue={setItemValue}
                        data={data}
                        models={models} />
                </Col>
            </Row>
        </form>
    )
}

export default ItemForm
