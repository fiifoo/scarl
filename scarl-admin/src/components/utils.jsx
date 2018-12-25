import React from 'react'

export const createComponent = (Component, factoryProps) => (
    props => (
        <Component {...factoryProps} {...props} />
    )
)
