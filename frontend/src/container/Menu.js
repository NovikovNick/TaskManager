import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../store/ReduxActions";
import {Dropdown} from "react-bootstrap";
import {useTranslation} from "react-i18next";

import ProfileModalForm from "../component/form/ProfileModalForm";
import {useHistory} from "react-router-dom";
import useModal from "../hook/useModal";

function Menu({actions, user}) {

    useEffect(() => {
        actions.getUserProfile();
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

const Signout = connect(null, mapDispatchToProps)(function Signout({actions}) {
    const {t} = useTranslation();
    const history = useHistory();
    const signout = () => actions.signOut().then(() => history.push("/signin"))

    return (<Dropdown.Item onClick={signout}>{t("signout")}</Dropdown.Item>);
})

export default connect(mapStateToProps, mapDispatchToProps)(Menu);