import React from 'react';
import ReactDOM from 'react-dom';

import 'bootstrap/dist/js/bootstrap.bundle';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'reset-css';
import 'normalize.css/normalize.css';
import './css/index.scss';

import "./i18n";
import App from './App';


const rootElement = document.getElementById("root");
ReactDOM.render(
<App />,
    rootElement
);