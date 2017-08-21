import moment from 'moment'

export const formatDateTime = value => moment(value).format('D.M.YYYY H:mm')
