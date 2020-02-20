import React from 'react';
import {Container} from "react-bootstrap";
import ChangePasswordForm from "../component/form/ChangePasswordForm";


export default function ChangePasswordPage() {
    return (
        <Container fluid>
            <div className="change_password_panel_wrapper">
                <div className="login-reg-panel">
                    <div className={"white-panel"}>

                        <ChangePasswordForm/>

                    </div>
                </div>
            </div>
        </Container>
    );
}
