import React, { Component } from 'react'
import { Modal } from 'react-bootstrap'

class LazyModal extends Component {

    shouldComponentUpdate(nextProps) {
        return this.props.visible || nextProps.visible
    }

    render() {
        const {component, title, visible, modalProps, ...props} = this.props
        const {focusKeyboard, toggle} = this.props
        const Component = component

        const onHide = () => {
            toggle()
            focusKeyboard()
        }

        return (
            <Modal show={visible} onHide={onHide} animation={false} restoreFocus={false} {...modalProps}>
                <Modal.Header closeButton>
                    <Modal.Title>{title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Component {...props} />
                </Modal.Body>
            </Modal>
        )
    }
}

export default LazyModal
