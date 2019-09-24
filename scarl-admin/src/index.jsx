import 'bootstrap/dist/css/bootstrap.css'
import React from 'react'
import ReactDOM from 'react-dom'
import { Provider } from 'react-redux'
import { createStore, applyMiddleware } from 'redux'
import { composeWithDevTools } from 'redux-devtools-extension'
import thunk from 'redux-thunk'
import initialState from './initialState'
import AppContainer from './components/AppContainer'
import reducer from './reducers/index'

const store = createStore(
    reducer,
    initialState,
    composeWithDevTools(applyMiddleware(thunk))
)

const app = (
    <Provider store={store}>
        <AppContainer />
    </Provider>
)

ReactDOM.render(
    app,
    document.getElementById('root')
)
