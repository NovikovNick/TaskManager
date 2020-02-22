import React from 'react';

import RunningList from "../../container/RunningList";
import {Container} from "react-bootstrap";
import Menu from "../../container/Menu";
import Authenticator from "../../container/Authenticator";
import "./styles.scss";


export default function MainPage() {
    return (
        <Authenticator path={"/"}>
            <Container fluid className={"h-100 position-absolute"}>
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