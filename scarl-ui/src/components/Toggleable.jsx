import React from 'react'

const Toggleable = ({component, visible, ...props}) => {
    const Component = component

    return (
        <div style={{display: visible ? 'block' : 'none'}}>
            <Component {...props} />
        </div>
    )
}

export default Toggleable
