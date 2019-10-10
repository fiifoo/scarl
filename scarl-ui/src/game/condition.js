export const getConditionLabel = condition => (
    labels[condition] || condition.split('.')[1]
)

const labels = {}
