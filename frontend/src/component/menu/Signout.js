import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../../store/ReduxActions";
import {useTranslation} from "react-i18next";
import {useHistory} from "react-router-dom";
import {Dropdown} from "react-bootstrap";


function Signout({actions}) {
    const {t} = useTranslation();
    const history = useHistory();
    const signout = () => actions.signOut().then(() => history.push("/signin"));

    return (<Dropdown.Item onClick={signout}>{t("signout")}</Dropdown.Item>);
}

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(null, mapDispatchToProps)(Signout);
