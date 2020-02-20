import React, {useState} from 'react';
import {useHistory} from "react-router-dom";
import {useTranslation} from "react-i18next";
import * as Service from "../../service/service";
import {Button, Form, Row} from 'react-bootstrap';
import {Formik} from "formik";
import GoogleOAuth2Link from "../link/GoogleOAuth2Link";


export default function LoginForm() {
    const {t} = useTranslation();
    const history = useHistory();
    const [errors, setErrors] = useState({});

    const onSubmit = (values, {resetForm}) => {

        Service.signIn(values)
            .then(res => {
                resetForm({})
                history.push("/");
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

                    <Row>
                        <GoogleOAuth2Link />
                    </Row>
                </Form>
            )}
        </Formik>
    );
}