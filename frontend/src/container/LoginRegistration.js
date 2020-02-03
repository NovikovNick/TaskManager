import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Button, Fade, Form, Row} from 'react-bootstrap';
import * as REST from "../rest/rest";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faGoogle} from "@fortawesome/free-brands-svg-icons";
import {Formik} from "formik";

function Login() {
    const {t} = useTranslation();
    const [errors, setErrors] = useState({});

    const onSubmit = (values, {resetForm}) => {

        REST.signIn(values)
            .then(res => {
                resetForm({})
                window.location = "/";

            })
            .catch(() => setErrors({
                username: [t("Authentication failed")],
                password: [t("Authentication failed")]
            }));
    };

    return (
        <Formik
            enableReinitialize
            initialValues={{username: '', password: ''}}
            onSubmit={onSubmit}>
            {({
                  handleChange,
                  handleSubmit,
                  values,
                  touched
              }) => (

                <Form noValidate onSubmit={handleSubmit}>

                    <h2>{t("Login")}</h2>

                    <Form.Group as={Row} controlId="validationFormik01">
                        <Form.Control
                            name="username"
                            onChange={(v) => {
                                if (errors && errors.username) {
                                    const { username, ...rest } = errors;
                                    setErrors(rest);
                                }
                                handleChange(v);
                            }}
                            defaultValue={values.username}
                            isValid={touched.username && !errors.username}
                            isInvalid={!!errors.username}
                            placeholder={t("Username")}
                        />
                        <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group as={Row} controlId="validationFormik02">
                        <Form.Control
                            type="password"
                            name="password"
                            onChange={(v) => {
                                if (errors && errors.password) {
                                    const { password, ...rest } = errors;
                                    setErrors(rest);
                                }
                                handleChange(v);
                            }}
                            defaultValue={values.password}
                            isValid={touched.password && !errors.password}
                            isInvalid={!!errors.password}
                            placeholder={t("Password")}
                        />
                        <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                    </Form.Group>

                    <Row><Button type="submit">{t('Sign in')}</Button></Row>

                    <Row className={"d-block p-3 text-center"}>{t('or connect with')}</Row>
                    <Social/>
                </Form>
            )}
        </Formik>
    );
}
function Social() {
    const {t} = useTranslation();

    return (
        <Row>
            <Button className={"login-google"} href={"https://runninglist.ru:8443/oauth2/authorization/google"}>
                <FontAwesomeIcon icon={faGoogle}/> Google
            </Button>
        </Row>
    );
}

function Registration() {
    const {t} = useTranslation();

    const [errors, setErrors] = useState({});
    const [valid, setValid] = useState({});

    const STATE = {
        FORM: "FORM",
        EMAIL_SENT: "EMAIL_SENT",
        SERVER_ERROR: "SERVER_ERROR"
    }

    const [state, setState] = useState(STATE.FORM);

    const onSubmit = (values, {resetForm}) => {

        REST.signUp(values)
            .then(res => {

                resetForm({})
                if(res) {
                    setState(STATE.EMAIL_SENT)
                } else {
                    setState(STATE.SERVER_ERROR)
                }

            })
            .catch(response => {
                setErrors(response)

                // if key doesn't present in errors, then it is valid
                var valid = Object.keys(values).reduce(function(obj, k) {
                    if (!response.hasOwnProperty(k)) obj[k] = values[k];
                    return obj;
                }, {});
                setValid(valid);
            });
    };

    const success = (<div className={"text-center result-panel"}>
        <h2>{t("Check your email inbox")}</h2>
        <p>{t("We sent an email link to complite your registration")}</p>
    </div>);

    const fail = (<div className={"text-center result-panel"}>
        <h2>{t("Server error")}</h2>
        <p>{t("Email server is not available at the moment")}</p>
    </div>);

    const form = (<Formik
        enableReinitialize
        initialValues={{username: '', email: '', password: '', confirmPassword: ''}}
        onSubmit={onSubmit}
        initialErrors={errors}
    >
        {({
              handleChange,
              handleSubmit,
              values,
              touched
          }) => (

            <Form noValidate onSubmit={handleSubmit}>

                <h2>{t("Registration")}</h2>

                <Form.Group as={Row} controlId="validationFormik11">
                    <Form.Control
                        name="username"
                        onChange={(v) => {
                            if (errors && errors.username) {
                                const {username, ...rest} = errors;
                                setErrors(rest);
                            }
                            if (valid && valid.username) {
                                const {username, ...rest} = valid;
                                setValid(rest);
                            }
                            handleChange(v);
                        }}
                        defaultValue={values.username}
                        isValid={touched.username && valid.username}
                        isInvalid={!!errors.username}
                        placeholder={t("Username")}
                    />
                    <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group as={Row} controlId="validationFormik12">
                    <Form.Control
                        name="email"
                        onChange={(v) => {
                            if (errors && errors.email) {
                                const {email, ...rest} = errors;
                                setErrors(rest);
                            }
                            if (valid && valid.email) {
                                const {email, ...rest} = valid;
                                setValid(rest);
                            }
                            handleChange(v);
                        }}
                        defaultValue={values.email}
                        isValid={touched.email && valid.email}
                        isInvalid={!!errors.email}
                        placeholder={t("Email")}
                    />
                    <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group as={Row} controlId="validationFormik13">
                    <Form.Control
                        type="password"
                        name="password"
                        onChange={(v) => {
                            if (errors && errors.password) {
                                const {password, ...rest} = errors;
                                setErrors(rest);
                            }
                            if (valid && valid.password) {
                                const {password, ...rest} = valid;
                                setValid(rest);
                            }
                            handleChange(v);
                        }}
                        defaultValue={values.password}
                        isValid={touched.password && valid.password}
                        isInvalid={!!errors.password}
                        placeholder={t("Password")}
                    />
                    <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group as={Row} controlId="validationFormik14">
                    <Form.Control
                        type="password"
                        name="confirmPassword"
                        onChange={(v) => {
                            if (errors && errors.confirmPassword) {
                                const {confirmPassword, ...rest} = errors;
                                setErrors(rest);
                            }
                            if (valid && valid.confirmPassword) {
                                const {confirmPassword, ...rest} = valid;
                                setValid(rest);
                            }
                            handleChange(v);
                        }}
                        defaultValue={values.confirmPassword}
                        isValid={touched.confirmPassword && valid.confirmPassword}
                        isInvalid={!!errors.confirmPassword}
                        placeholder={t("Confirm password")}
                    />
                    <Form.Control.Feedback type="invalid">{errors.confirmPassword}</Form.Control.Feedback>
                </Form.Group>

                <Row>
                    <Button type="submit">{t('Sign up')}</Button>
                </Row>


                <Row className={"d-block p-3 text-center"}>{t('Or sign up using...')}</Row>
                <Social/>
            </Form>
        )}
    </Formik>);

    switch (state) {
        case STATE.EMAIL_SENT:
            return success;
        case STATE.SERVER_ERROR:
            return fail;
        default:
            return form;
    }
}

export default function LoginRegistration() {

    const {t} = useTranslation();
    const [onRegistration, switchForm] = useState(false);

    return (
        <div className="login-reg-panel_wrapper">

            <video style={{float: "right"}} loop autoPlay>
                <source src="/video/office.mp4" type="video/mp4" />
                Your browser does not support the video tag.
            </video>

            <div className="login-reg-panel">

                <Fade in={onRegistration}>
                    <div className={"login-info-box"}>
                        <h2>{t("Have an account?")}</h2>
                        <Button bsPrefix="switch-btn" onClick={() => switchForm(false)}>{t("Login")}</Button>
                    </div>
                </Fade>

                <Fade in={!onRegistration}>
                    <div className={"register-info-box"}>
                        <h2>{t("Don't have an account?")}</h2>
                        <Button bsPrefix="switch-btn" onClick={() => switchForm(true)}>{t("Register")}</Button>
                    </div>
                </Fade>

                <div className={onRegistration ? "white-panel right-log" : "white-panel"}>

                    <div className={!onRegistration ? "login-show show-log-panel" : "login-show"}>
                        <Login/>
                    </div>

                    <div className={onRegistration ? "register-show show-log-panel" : "register-show"}>
                        <Registration/>
                    </div>

                </div>
            </div>
        </div>
    );
}