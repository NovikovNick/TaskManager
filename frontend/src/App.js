import React, {Suspense} from 'react';
import {combineReducers, createStore} from 'redux';
import {Provider} from 'react-redux';
import {BrowserRouter as Router, Route} from "react-router-dom";

import {toast, ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import * as reducers from './store/reducers';
import * as REST from "./rest/rest";
import * as Store from "./store/ReduxActions";

import Page from "./page/Page";
import LoginPage from "./page/LoginPage";
import ChangePasswordPage from "./page/ChangePasswordPage";
import ExpiredTokenPage from "./page/ExpiredTokenPage";

const store = createStore(combineReducers(reducers),
    window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__());

toast.configure();

export function getTaskList() {
    REST.getTaskList()
        .then(taskList => {
            store.dispatch(Store.setRunningList(taskList));
        });
}

export function setTaskList(taskList) {
    store.dispatch(Store.setRunningList(taskList));
}


export default function App() {

    return (
        <Provider store={store}>
            <ToastContainer
                autoClose={1500}
                hideProgressBar={true}/>

            <Suspense fallback={<div>loading...</div>}>

                <Router>
                    <Route path="/expired/token" component={ExpiredTokenPage}/>
                    <Route path="/signin" component={LoginPage}/>
                    <Route path="/changepassword" component={ChangePasswordPage}/>
                    <Route exact path="/" component={Page}/>
                </Router>

            </Suspense>

        </Provider>
    );
}