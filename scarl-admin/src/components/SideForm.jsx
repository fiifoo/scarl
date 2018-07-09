import React from 'react'
import { getFieldComponent } from './field/utils'

const HideButton = props => (
    <button
        type="button"
        className="btn btn-xs btn-link"
        {...props}>
        &#x2715;
    </button>
)

const SideForm = ({sideForm, common}) => {
    if (! sideForm) {
        return <div />
    }

    const {data, hideSideForm} = common
    const {model, fieldType, path} = sideForm
    const Component = getFieldComponent(fieldType, model, false)

    const title = (
        <span>
            <div className="pull-right">
                <HideButton onClick={hideSideForm} />
            </div>
            {sideForm.model.id}
            <span className="text-muted" style={{paddingLeft: 20}}>
                {sideForm.path.join('.')}
            </span>
        </span>
    )

    const form = (
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

    return  (
        <div className="side-form">
            <h4>{title}</h4>
            <hr />
            {form}
        </div>
    )
}

export default SideForm
