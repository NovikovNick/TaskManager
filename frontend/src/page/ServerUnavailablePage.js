import React from 'react';
import {Container} from "react-bootstrap";
import Authenticator from "../container/Authenticator";
import ServerUnavailablePanel from "../component/panel/ServerUnavailablePanel";


export default function ServerUnavailablePage() {
    return (
        <Authenticator path={"/"}>
            <Container className={"h-100 d-flex"}>

                <ServerUnavailablePanel/>

            </Container>
        </Authenticator>
    );
}
