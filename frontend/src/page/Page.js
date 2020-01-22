import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Store from "../store/ReduxActions";

import RunningList from "../container/RunningList";

import * as REST from "../rest/rest";
import {Col, Container, Nav, Row} from "react-bootstrap";
import Profile from "../container/Profile";

class Page extends Component {
    render() {

        const signout = function () {
            REST.signOut().then(() => window.location = "/signin");
        }

        return (
            <Container fluid>

                <div className={"metalheart-sidebar"} >
                    <Profile/>
                </div>
                <div className={"metalheart-content"} >
                    <div className="metalheart-wrapper">
                        <RunningList/>
                    </div>
                </div>
            </Container>

        );
    }
}

const mapStateToProps = state => ({
    taskList: state.task.taskList
});

const mapDispatchToProps = (dispatch) => ({
    actions: bindActionCreators(Store, dispatch)
})

export default connect(mapStateToProps, mapDispatchToProps)(Page);