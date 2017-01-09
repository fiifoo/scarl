import 'bootstrap/dist/css/bootstrap.css'
import React from 'react'
import ReactDOM from 'react-dom'
import { Provider } from 'react-redux'
import { createStore, applyMiddleware } from 'redux'
import thunk from 'redux-thunk'
import reducer from './reducers/index'
import App from './components/App.jsx'

const store = createStore(
    reducer,
    applyMiddleware(thunk)
)

const app = (
    <Provider store={store}>
        <App />
    </Provider>
)

ReactDOM.render(
    app,
    document.getElementById('root')
)
