import React from 'react'

const TemplateSummary = ({templates}) => {

    const renderTemplate = (percentage, template) => (
        <tr key={template}>
            <td>{template}</td>
            <td>{percentage}</td>
        </tr>
    )

    const renderArea = (templates, areaId) => (
        <tbody key={areaId}>
            <tr>
                <th>{areaId}</th>
                <th style={{width: 100}}>Success %</th>
            </tr>
            {templates
                .sortBy((_, key) => key, (a, b) => a < b ? -1 : 1)
                .map(renderTemplate)
                .toArray()
            }
        </tbody>
    )

    return (
        <table className="table table-condensed table-striped table-hover">
            {templates
                .sortBy((_, key) => key, (a, b) => a < b ? -1 : 1)
                .map(renderArea)
                .toArray()
            }
        </table>
    )
}

export default TemplateSummary
