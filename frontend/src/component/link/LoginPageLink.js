import React from "react";
import {useTranslation} from "react-i18next";
import {useHistory} from "react-router-dom";
import Button from "react-bootstrap/Button";


export default function LoginPageLink() {

    const {t} = useTranslation();
    const history = useHistory();

    return (
        <Button onClick={() => history.push("/signin")}>
            {t("Return to login page")}
        </Button>
    )
}
