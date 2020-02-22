import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../store/ReduxActions";
import * as Service from "../service/service";
import {Dropdown} from "react-bootstrap";
import {useTranslation} from "react-i18next";

import ProfileModalForm from "../component/form/ProfileModalForm";
import {useHistory} from "react-router-dom";
import useModal from "../hook/useModal";

function Menu({actions, user}) {

    useEffect(() => {
        Service.getUserProfile().then(actions.setUser);
    }, []);

    return (
        <Dropdown className={"metalheart-menu w-100"}>

            <Dropdown.Toggle split id="dropdown-split-basic">{user.username}</Dropdown.Toggle>

            <Dropdown.Menu>
                <Profile />
                <Signout />
            </Dropdown.Menu>

        </Dropdown>
    )
}

function Signout() {
    const {t} = useTranslation();
    const history = useHistory();
    const signout = () => Service.signOut().then(() => history.push("/signin"))

    return (<Dropdown.Item onClick={signout}>{t("signout")}</Dropdown.Item>);
}

function Profile() {

    const {isActive, toggle} = useModal();
    const {t} = useTranslation();

    return (
        <span>
            <Dropdown.Item onClick={toggle}>{t("Profile")}</Dropdown.Item>
            <ProfileModalForm
                active={isActive}
                onCloseForm={toggle}
            />
        </span>
    );
}

const mapStateToProps = state => ({
    user: state.task.user
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(Menu);