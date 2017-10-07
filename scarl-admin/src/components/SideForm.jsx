import React from 'react'
import { Modal } from 'react-bootstrap'
import { getFieldComponent } from './field/utils'

const SideForm = ({sideForm, common}) => {
    const {data, hideSideForm} = common

    const renderForm = () => {
        const {model, fieldType, path} = sideForm

        const Component = getFieldComponent(fieldType, model, false)

        return (
            <div className="form-horizontal item-form">
                <Component
                    required={true}
                    model={model}
                    fieldType={fieldType}
                    path={path}
                    value={data.getIn(path)}
                    common={common} />
            </div>
        )
    }

    const title = sideForm && (
        <span>
            {sideForm.model.id}
            <span className="text-muted" style={{paddingLeft: 20}}>
                {sideForm.path.join('.')}
            </span>
        </span>
    )

    return  (
        <Modal show={!!sideForm} onHide={hideSideForm} bsSize="large" animation={false}>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {sideForm ? renderForm() : null}
            </Modal.Body>
        </Modal>
    )
}

export default SideForm
