import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Button, Fade} from 'react-bootstrap';
import ForgetPasswordForm from "../../component/form/ForgetPasswordForm";
import LoginForm from "../../component/form/LoginForm";
import RegistrationForm from "../../component/form/RegistrationForm";
import "./styles.scss";

export default function LoginRegistration() {

    const {t} = useTranslation();
    const [onRegistration, switchForm] = useState(false);
    const [onForgetPasswordForm, setOnForgetPasswordForm] = useState(false);

    const switchForgetPasswordForm = (state) => {
        switchForm(state);
        setOnForgetPasswordForm(state);
    }

    return (
        <div className="login-reg-panel_wrapper">

            <div className="login-reg-panel">

                <Fade in={onRegistration && !onForgetPasswordForm}>
                    <div className={"login-info-box"}>
                        <h2>{t("Have an account?")}</h2>
                        <Button bsPrefix="switch-btn" onClick={() => switchForm(false)}>{t("Login")}</Button>
                    </div>
                </Fade>

                <Fade in={onRegistration && onForgetPasswordForm}>
                    <div className={"login-info-box"}>
                        <Button bsPrefix="switch-btn" onClick={() => switchForgetPasswordForm(false)}>{t("Back")}</Button>
                    </div>
                </Fade>

                <Fade in={!onRegistration}>
                    <div>
                        <div className={"register-info-box"}>
                            <h2>{t("Don't have an account?")}</h2>
                            <Button bsPrefix="switch-btn" onClick={() => switchForm(true)}>{t("Register")}</Button>
                            <hr/>
                            <h2>{t("Forgot your password")}</h2>
                            <Button bsPrefix="switch-btn" onClick={() => switchForgetPasswordForm(true)}>{t("Reset")}</Button>
                        </div>
                    </div>
                </Fade>

                <div className={onRegistration ? "white-panel right-log" : "white-panel"}>

                    <div className={!onRegistration ? "login-show show-log-panel" : "login-show"}>
                        <LoginForm />
                    </div>

                    <div className={onRegistration ? "register-show show-log-panel" : "register-show"}>
                        {onForgetPasswordForm ? <ForgetPasswordForm /> : <RegistrationForm /> }
                    </div>

                </div>
            </div>
        </div>
    );
}