import React from 'react';
import {useHistory} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {Button} from 'react-bootstrap';


export default function ChangePasswordPageLink() {

    const {t} = useTranslation();
    const history = useHistory();

    return (
        <Button onClick={() => history.push("/changepassword")}>
            {t("Send email to change password")}
        </Button>
    );
}