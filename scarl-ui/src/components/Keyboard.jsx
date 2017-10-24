import React, { Component } from 'react'

class Keyboard extends Component {

    componentDidMount() {
        if (this.props.keyboardFocused) {
            this.focus()
        }
    }

    componentDidUpdate() {
        if (this.props.keyboardFocused) {
            this.focus()
        }
    }

    focus() {
        this.element.focus()
    }

    render() {

        const keypress = event => {
            event.preventDefault()
            if (this.props.game.running) {
                this.props.keypress(event)
            }
        }

        const style = {margin: 0, padding: 0, border: 0,  width: 0, height: 0, float: 'left'}

        return (
            <input
                type="input"
                style={style}
                onBlur={this.props.blurKeyboard}
                onKeyDown={keypress}
                ref={input => this.element = input} />
        )
    }
}

export default Keyboard
