import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../store/ReduxActions";
import * as Service from "../service/service";
import {Dropdown} from "react-bootstrap";
import {useTranslation} from "react-i18next";

import ProfileModalForm from "../component/form/ProfileModalForm";
import {useHistory} from "react-router-dom";

function Menu({actions, user}) {

    const [profileModalFormActive, setProfileModalFormActive] = useState(false);

    useEffect(() => {
        Service.getUserProfile().then(actions.setUser);
    }, []);

    return (
        <Dropdown className={"metalheart-menu w-100"}>

            <Dropdown.Toggle split id="dropdown-split-basic">{user.username}</Dropdown.Toggle>

            <Dropdown.Menu>
                <Profile onClick={() => setProfileModalFormActive(true)}/>
                <Signout/>
            </Dropdown.Menu>
            <ProfileModalForm
                active={profileModalFormActive}
                onCloseForm={() => setProfileModalFormActive(false)}
            />
        </Dropdown>
    )
}

function Signout() {
    const {t} = useTranslation();
    const history = useHistory();
    const signout = () => Service.signOut().then(() => history.push("/signin"))

    return (<Dropdown.Item onClick={signout}>{t("signout")}</Dropdown.Item>);
}

function Profile({onClick}) {
    const {t} = useTranslation();
    return (<Dropdown.Item onClick={onClick}>{t("Profile")}</Dropdown.Item>);
}

const mapStateToProps = state => ({
    user: state.task.user
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(Menu);