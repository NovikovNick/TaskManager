import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Button, Fade, Form, Row} from 'react-bootstrap';
import * as REST from "../rest/rest";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faGoogle} from "@fortawesome/free-brands-svg-icons";
import {Formik} from "formik";

function Login() {
    const {t} = useTranslation();

    const onSubmit = (values, {setErrors, resetForm}) => {

        REST.signIn(values)
            .then(res => {
                window.location = "/";
                resetForm({})
            })
            .catch(setErrors);
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
                  errors,
                  touched
              }) => (

                <Form noValidate onSubmit={handleSubmit}>

                    <h2>{t("Login")}</h2>

                    <Form.Group as={Row} controlId="validationFormik01">
                        <Form.Control
                            name="username"
                            onChange={handleChange}
                            defaultValue={values.username}
                            isValid={touched.username && !errors.username}
                            isInvalid={!!errors.username}
                            placeholder={t("Username")}
                        />
                        <Form.Control.Feedback>{t('Looks good!')}</Form.Control.Feedback>
                        <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group as={Row} controlId="validationFormik02">
                        <Form.Control
                            type="password"
                            name="password"
                            onChange={handleChange}
                            defaultValue={values.password}
                            isValid={touched.password && !errors.password}
                            isInvalid={!!errors.password}
                            placeholder={t("Password")}
                        />
                        <Form.Control.Feedback type="valid">{t('Looks good!')}</Form.Control.Feedback>
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
            <Button className={"login-google"} href={"http://runninglist.ru:8080/oauth2/authorization/google"}>
                <FontAwesomeIcon icon={faGoogle}/> Google
            </Button>
        </Row>
    );
}

function Registration() {
    const {t} = useTranslation();

    const onSubmit = (values, {setErrors, resetForm}) => {

        REST.signUp(values)
            .then(res => {
                window.location = "/";
                resetForm({})
            })
            .catch(setErrors);
    };

    return (
        <Formik
            enableReinitialize
            initialValues={{username: '', email: '', password: '', confirmPassword: ''}}
            onSubmit={onSubmit}>
            {({
                  handleChange,
                  handleSubmit,
                  values,
                  errors,
                  touched
              }) => (

                <Form noValidate onSubmit={handleSubmit}>

                    <h2>{t("Registration")}</h2>

                    <Form.Group as={Row} controlId="validationFormik11">
                        <Form.Control
                            name="username"
                            onChange={handleChange}
                            defaultValue={values.username}
                            isValid={touched.username && !errors.username}
                            isInvalid={!!errors.username}
                            placeholder={t("Username")}
                        />
                        <Form.Control.Feedback>{t('Looks good!')}</Form.Control.Feedback>
                        <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group as={Row} controlId="validationFormik12">
                        <Form.Control
                            name="email"
                            onChange={handleChange}
                            defaultValue={values.email}
                            isValid={touched.email && !errors.email}
                            isInvalid={!!errors.email}
                            placeholder={t("Email")}
                        />
                        <Form.Control.Feedback>{t('Looks good!')}</Form.Control.Feedback>
                        <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group as={Row} controlId="validationFormik13">
                        <Form.Control
                            type="password"
                            name="password"
                            onChange={handleChange}
                            defaultValue={values.password}
                            isValid={touched.password && !errors.password}
                            isInvalid={!!errors.password}
                            placeholder={t("Password")}
                        />
                        <Form.Control.Feedback type="valid">{t('Looks good!')}</Form.Control.Feedback>
                        <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group as={Row} controlId="validationFormik14">
                        <Form.Control
                            type="password"
                            name="confirmPassword"
                            onChange={handleChange}
                            defaultValue={values.confirmPassword}
                            isValid={touched.confirmPassword && !errors.confirmPassword}
                            isInvalid={!!errors.confirmPassword}
                            placeholder={t("Confirm password")}
                        />
                        <Form.Control.Feedback type="valid">{t('Looks good!')}</Form.Control.Feedback>
                        <Form.Control.Feedback type="invalid">{errors.confirmPassword}</Form.Control.Feedback>
                    </Form.Group>

                    <Row>
                        <Button type="submit">{t('Sign up')}</Button>
                    </Row>


                    <Row className={"d-block p-3 text-center"}>{t('Or sign up using...')}</Row>
                    <Social/>
                </Form>
            )}
        </Formik>
    );
}

export default function LoginRegistration() {

    const {t} = useTranslation();
    const [onRegistration, switchForm] = useState(false);

    return (
        <div className="login-reg-panel_wrapper">

            <video style={{float: "right"}} loop autoPlay>
                <source src="/vidio/office.mp4" type="video/mp4" />
                Your browser does not support the video tag.
            </video>

            <div className="login-reg-panel">

                <Fade in={onRegistration}>
                    <div className={"login-info-box"}>
                        <h2>Have an account?</h2>
                        <Button bsPrefix="switch-btn" onClick={() => switchForm(false)}>Login</Button>
                    </div>
                </Fade>

                <Fade in={!onRegistration}>
                    <div className={"register-info-box"}>
                        <h2>Don't have an account?</h2>
                        <Button bsPrefix="switch-btn" onClick={() => switchForm(true)}>Register</Button>
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