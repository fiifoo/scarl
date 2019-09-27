export const getStanceDescriptions = stance => (
    descriptions[stance] || []
)

export const getStanceLabel = stance => (
    labels[stance] || stance.split('.')[1]
)

export const getStanceSelection = stances => number => (
    number === 5 ? (
        null
    ) : (
        number < 5 ? (
            stances[number - 1]
        ) : (
            stances[number]
        )
    )
)

export const isChangeStanceAllowed = (creature, stance = undefined) => (
    creature.stances.find(x => (stance === undefined || x.key !== stance) && (
        x.duration != null && x.duration !== x.initialDuration
    )) === undefined
)

const descriptions = {
    'Stances.Aim': [
        'Ranged attack +20%',
        'Defence -20%',
        'Speed -50%',
        'Minimum turns 2',
    ],
    'Stances.Evasive': [
        'Defence +20%',
        'Melee attack -20%',
        'Ranged attack -20%',
    ],
}

const labels = {}
