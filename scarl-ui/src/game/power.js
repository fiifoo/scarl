import { List, OrderedMap } from 'immutable'

export const resourceStats = OrderedMap([
    [['resources', 'health'], 'Health'],
    [['resources', 'energy'], 'Energy'],
    [['resources', 'materials'], 'Materials'],
]).mapKeys(path => List(path))
