import { connect } from 'react-redux'
import { focusKeyboard } from '../actions/keyboardActions'
import Screen from '../components/Screen.jsx'

const ScreenContainer = connect(
    state => ({
        area: state.area,
        fov: state.fov,
        game: state.game,
        kinds: state.kinds,
        map: state.map,
    }),
    {focusKeyboard}
)(Screen)

export default ScreenContainer
