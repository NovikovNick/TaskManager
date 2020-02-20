import React from 'react';
import {useTranslation} from "react-i18next";
import Jumbotron from "react-bootstrap/Jumbotron";
import LoginPageLink from "../link/LoginPageLink";


export default function ExpiredTokenPanel() {

    const {t} = useTranslation();

    return (
        <Jumbotron className={"my-auto w-100 text-center"}>
            <h1>{t("Token expired")}</h1>
            <p>{t("Sorry, your token expired")}</p>
            <p className={"p-4"}>
                <LoginPageLink/>
            </p>
        </Jumbotron>
    );
}
