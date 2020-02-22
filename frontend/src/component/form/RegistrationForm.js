import React, {useState} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import {useTranslation} from "react-i18next";
import {Button, Form, Row} from 'react-bootstrap';
import {Formik} from "formik";
import GoogleOAuth2Link from "../link/GoogleOAuth2Link";
import * as Store from "../../store/ReduxActions";


function RegistrationForm({actions}) {
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

        actions.signUp(values)
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

                <Row>
                    <GoogleOAuth2Link />
                </Row>

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

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(null, mapDispatchToProps)(RegistrationForm);