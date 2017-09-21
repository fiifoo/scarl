import React from 'react'

const FormRow = ({children, label, horizontal = true}) => (
    label === undefined ? (
        horizontal ? (
            <div className="form-group">
                <div className="col-md-12">{children}</div>
            </div>
        ) : (
            <div className="form-group">
                {children}
            </div>
        )
    ) : (
        horizontal ? (
            <div className="form-group">
                <label className="col-md-2 control-label">{label}</label>
                <div className="col-md-10">{children}</div>
            </div>
        ) : (
            <div className="form-group">
                <label>{label}</label>
                {children}
            </div>
        )
    )
)

export default FormRow
