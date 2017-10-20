import React from 'react'

const FormRow = ({children, label, horizontal = true, error = false}) => {
    const className = error ? 'form-group has-error' : 'form-group'

    return (
        label === undefined ? (
            horizontal ? (
                <div className={className}>
                    <div className="col-md-12">{children}</div>
                </div>
            ) : (
                <div className={className}>
                    {children}
                </div>
            )
        ) : (
            horizontal ? (
                <div className={className}>
                    <label className="col-md-2 control-label">{label}</label>
                    <div className="col-md-10">{children}</div>
                </div>
            ) : (
                <div className={className}>
                    <label>{label}</label>
                    {children}
                </div>
            )
        )
    )
}

export default FormRow
