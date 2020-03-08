import React, {useEffect, useState} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import setting from "../../config";
import {useHistory} from "react-router-dom";
import LoadingOverlay from "react-loading-overlay";
import "./styles.scss";
import * as Store from "../../store/ReduxActions";


function Authenticator({path, children, user, actions}) {

    const isAuthenticated = !!user.username;

    const [responseReceived, setResponseReceived] = useState(isAuthenticated);
    const history = useHistory();

    useEffect(() => {

        const settings = {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Accept': 'application/json',
                'TIMEZONE_OFFSET': new Date().getTimezoneOffset()
            },
            cache: 'no-cache'
        };
        isAuthenticated || fetch(setting.API_URL + '/runninglist/data', settings)
            .then(response => {

                setResponseReceived(true);

                switch (response.status) {
                    case 200:
                        new Promise((resolve) => response.json()
                            .then((json) => resolve(json)))
                            .then(res => {
                                res.user && actions.setUser(res.user);
                                res.runningList && actions.setRunningList(res.runningList);
                                res.archives && actions.setArchives(res.archives);
                            })
                        history.push(path)
                        break;
                    case 403:
                        history.push("/signin")
                        break;
                    default:
                        history.push("/error")
                }
            })
            .catch(error => {
                setResponseReceived(true);
                history.push("/error")
            });
    }, [])


    return (<LoadingOverlay active={!responseReceived} spinner>

        {children}

    </LoadingOverlay>)
}

const mapStateToProps = state => ({
    user: state.task.user
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(Authenticator);
