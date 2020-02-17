import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Store from "../store/ReduxActions";

import RunningList from "../container/RunningList";
import {Container} from "react-bootstrap";
import Menu from "../container/Menu";
import Authenticator from "../container/Authenticator";

class Page extends Component {
    render() {
        return (
            <Authenticator path={"/"}>
                <Container fluid>
                    <div className={"metalheart-sidebar"}>
                        <Menu/>
                    </div>
                    <div className={"metalheart-content"}>
                        <div className="metalheart-wrapper">
                            <RunningList/>
                        </div>
                    </div>
                </Container>
            </Authenticator>
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