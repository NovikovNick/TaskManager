import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../store/ReduxActions";
import * as REST from "../rest/rest";
import {Dropdown} from "react-bootstrap";
import {useTranslation} from "react-i18next";

class Profile extends Component {

    constructor(props) {
        super(props);

        REST.getUserProfile().then(props.actions.setUser);
    }

    render() {

        const {user} = this.props;

        return (
            <Dropdown className={"taskmanager-profile w-100"}>

                <Dropdown.Toggle split id="dropdown-split-basic">{user.username}</Dropdown.Toggle>

                <Dropdown.Menu>
                    <Signout/>
                </Dropdown.Menu>
            </Dropdown>
        )
    }
}

function Signout() {
    const {t} = useTranslation();
    const signout = () => REST.signOut().then(() => window.location = "/signin")

    return (<Dropdown.Item onClick={signout}>{t("signout")}</Dropdown.Item>);
}

const mapStateToProps = state => ({
    user: state.task.user
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(Profile);