import React from 'react';

import RunningList from "../container/RunningList/index";
import {Container} from "react-bootstrap";
import Menu from "../container/Menu/index";
import Authenticator from "../container/Authenticator/index";

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