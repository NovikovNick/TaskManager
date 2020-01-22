import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../store/ReduxActions";
import * as REST from "../rest/rest";
import {Col, Row, Dropdown} from "react-bootstrap";

class Profile extends Component {

    constructor(props) {
        super(props);

        REST.getUserProfile().then(props.actions.setUser);
    }

    signout = () => REST.signOut().then(() => window.location = "/signin");

    render() {

        const {user} = this.props;

        return (
            <Dropdown className={"taskmanager-profile w-100"}>

                <Dropdown.Toggle split id="dropdown-split-basic">  {user.username} </Dropdown.Toggle>

                <Dropdown.Menu>
                    <Dropdown.Item onClick={this.signout}>signout</Dropdown.Item>
                </Dropdown.Menu>
            </Dropdown>
        )
    }
}

const mapStateToProps = state => ({
    user: state.task.user
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(Profile);