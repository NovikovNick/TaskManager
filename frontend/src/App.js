import React, {Suspense} from 'react';
import {combineReducers, createStore, applyMiddleware} from 'redux';
import {Provider} from 'react-redux';
import thunk from 'redux-thunk';

import {BrowserRouter as Router, Route} from "react-router-dom";
import * as reducers from './store/reducers';
import Loadable from 'react-loadable';
import LoadingPanel from "./component/panel/LoadingPanel";
import PopupMessages from "./component/vendor/PopupMessages";

const store = createStore(combineReducers(reducers), applyMiddleware(thunk)
/* , window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__() */
);


export default function App() {

    return (
        <Provider store={store}>

            <PopupMessages/>

            <Suspense fallback={<div>loading...</div>}>

                <Router>

                    <Route exact path="/" component={Loadable({
                        loader: () => import("./page/MainPage"), loading: LoadingPanel,
                    })}/>

                    <Route path="/error" component={Loadable({
                        loader: () => import("./page/ServerUnavailablePage"), loading: LoadingPanel,
                    })}/>

                    <Route path="/expired/token" component={Loadable({
                        loader: () => import("./page/ExpiredTokenPage"), loading: LoadingPanel,
                    })}/>

                    <Route path="/signin" component={Loadable({
                        loader: () => import("./page/LoginPage"), loading: LoadingPanel,
                    })}/>

                    <Route path="/changepassword" component={Loadable({
                        loader: () => import("./page/ChangePasswordPage/index"), loading: LoadingPanel,
                    })}/>

                </Router>
            </Suspense>
        </Provider>
    );
}
