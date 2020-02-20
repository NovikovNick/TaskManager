import React from 'react';
import {Button} from 'react-bootstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faGoogle} from "@fortawesome/free-brands-svg-icons";


export default function GoogleOAuth2Link() {
    return (
        <Button className={"login-google"} href={"https://runninglist.ru:8443/oauth2/authorization/google"}>
            <FontAwesomeIcon icon={faGoogle}/> Google
        </Button>
    );
}