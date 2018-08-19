import React from 'react'
import TextInputRow from '../form/TextInputRow.jsx'
import { CirclePicker } from 'react-color'

const layerStyle = {
    position: 'absolute',
    backgroundColor: '#fff',
    padding: 5,
    boxShadow: '0px 2px 4px 0px rgba(0,0,0,0.3)',
    zIndex: 999,
}

const ColorBadge = ({color}) => (
    <span className="badge" style={{backgroundColor: color}}>&nbsp;</span>
)

const PureColorField = ({
    label, required, path, value, common,
    focused, onFocus, onBlur, onSelectStart, onSelectEnd
}) => {
    const {horizontal, setValue} = common

    const onChange = value => setValue(path, value)

    return (
        <div className="color-field">
            <TextInputRow
                horizontal={horizontal}
                label={label}
                required={required}
                value={value}
                addon={<ColorBadge color={value} />}
                onChange={onChange}
                onFocus={onFocus}
                onBlur={onBlur}>
                {focused && (
                    <div onMouseDown={onSelectStart} onClick={onSelectEnd} style={layerStyle}>
                        <CirclePicker
                            color={value || undefined}
                            onChangeComplete={x => onChange(x.hex)} />
                    </div>
                )}
            </TextInputRow>
        </div>
    )
}

class ColorField extends React.Component {
    constructor() {
        super()

        this.state = {focused: false, selecting: false}

        this.onFocus = () => this.setState({focused: true, selecting: false})
        this.onBlur = () => !this.state.selecting && this.setState({focused: false, selecting: false})
        this.onSelectStart = () => this.setState({focused: true, selecting: true})
        this.onSelectEnd = () => this.setState({focused: false, selecting: false})
    }

    render() {
        return <PureColorField
            {...this.props}
            focused={this.state.focused}
            onFocus={this.onFocus}
            onBlur={this.onBlur}
            onSelectStart={this.onSelectStart}
            onSelectEnd={this.onSelectEnd} />
    }
}

export default ColorField
