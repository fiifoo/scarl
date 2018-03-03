import React from 'react'

const TemplateSummary = ({templates}) => (
    <table className="table table-condensed table-striped table-hover">
        <thead>
            <tr>
                <th>Template</th>
                <th style={{width: 100}}>Success %</th>
            </tr>
        </thead>
        <tbody>
            {templates
                .sortBy((_, key) => key, (a, b) => a < b ? -1 : 1)
                .map((percentage, template) => (
                    <tr key={template}>
                        <td>{template}</td>
                        <td>{percentage}</td>
                    </tr>
                ))
                .toArray()
            }
        </tbody>
    </table>
)

export default TemplateSummary
