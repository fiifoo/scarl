import 'bootstrap/dist/css/bootstrap.css'
import React from 'react'
import ReactDOM from 'react-dom'
import { Provider } from 'react-redux'
import { createStore, applyMiddleware } from 'redux'
import thunk from 'redux-thunk'
import AppContainer from './containers/AppContainer'
import reducer from './reducers/index'
import initialState from './initialState'

const store = createStore(
    reducer,
    initialState,
    applyMiddleware(thunk)
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
