import React from 'react';
import {useTranslation} from "react-i18next";
import Jumbotron from "react-bootstrap/Jumbotron";


export default function ServerUnavailablePanel() {

    const {t} = useTranslation();

    return (
        <Jumbotron className={"my-auto w-100 text-center"}>
            <h1>{t("Unavailable server")}</h1>
            <p>{t("Sorry, server is not available")}</p>
        </Jumbotron>
    );
}