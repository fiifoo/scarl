import React from 'react'
import { Modal } from 'react-bootstrap'

const NoReferences = () => <i className="text-muted">No references</i>

const ItemReferences = ({references, hideItemReferences, showReferenceItem}) => {
    const title = references && (
        <span>
            References: {references.model.id} ({references.id})
        </span>
    )

    const renderReference = (reference, index) => (
        <div key={index}>
            <button
                type="button"
                className="btn btn-link"
                onClick={() => showReferenceItem(reference)}>
                {reference.join(' / ')}
            </button>
        </div>
    )

    return  (
        <Modal show={!!references} onHide={hideItemReferences} bsSize="large" animation={false}>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {references && !references.references.isEmpty() ? (
                    references.references.map(renderReference).toArray()
                ) : (
                    <NoReferences />
                )}
            </Modal.Body>
        </Modal>
    )
}

export default ItemReferences
