import React from 'react';

import RunningList from "../container/RunningList";
import {Container} from "react-bootstrap";
import Menu from "../container/Menu";
import Authenticator from "../container/Authenticator";

export default function MainPage() {
    return (
        <Authenticator path={"/"}>
            <Container fluid>
                <div className={"metalheart-sidebar"}>
                    <Menu/>
                </div>
                <div className={"metalheart-content"}>
                    <div className="metalheart-wrapper">
                        <RunningList/>
                    </div>
                </div>
            </Container>
        </Authenticator>
    );
}