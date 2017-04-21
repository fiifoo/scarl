import { connect } from 'react-redux'
import { focusKeyboard } from '../actions/keyboard'
import Screen from '../components/Screen.jsx'

const ScreenContainer = connect(
    state => ({
        area: state.area,
        cursor: state.ui.game.cursor,
        fov: state.fov,
        game: state.game,
        kinds: state.kinds,
        map: state.map,
    }),
    {focusKeyboard}
)(Screen)

export default ScreenContainer
