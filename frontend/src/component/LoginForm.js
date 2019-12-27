import React from 'react';
import {useTranslation} from "react-i18next";
import {Formik} from 'formik';
import {Button, Col, Form, Row} from 'react-bootstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faGoogle} from "@fortawesome/free-brands-svg-icons";

export default function LoginForm({schema}) {

    const {t} = useTranslation();

    const onSubmit = (values, {setErrors, resetForm}) => {

        schema.onSubmit(values)
            .then(res => {
                schema.onSuccess(res);
                resetForm({})
            })
            .catch(setErrors);
    };

    return (
        <Formik
            enableReinitialize
            initialValues={schema.formData}
            onSubmit={onSubmit}>

            {({
                  handleChange,
                  setFieldValue,
                  handleSubmit,
                  values,
                  errors,
                  touched,
                  resetForm
              }) => (
                <Form noValidate onSubmit={handleSubmit}>

                    <Form.Group as={Row} controlId="validationFormik01">
                        <Form.Label column sm="3">{t('username')}</Form.Label>
                        <Col sm={'9'}>
                            <Form.Control
                                name="username"
                                onChange={handleChange}
                                defaultValue={values.username}
                                isValid={touched.username && !errors.username}
                                isInvalid={!!errors.username}
                            />
                            <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
                            <Form.Control.Feedback type="invalid">
                                {errors.username}
                            </Form.Control.Feedback>
                        </Col>
                    </Form.Group>

                    <Form.Group as={Row} controlId="validationFormik02">
                        <Form.Label column sm="3">{t('password')}</Form.Label>
                        <Col sm={'9'}>
                            <Form.Control
                                type="password"
                                name="password"
                                onChange={handleChange}
                                defaultValue={values.password}
                                isValid={touched.password && !errors.password}
                                isInvalid={!!errors.password}
                            />
                            <Form.Control.Feedback type="valid">{t('Looks good!')}</Form.Control.Feedback>
                            <Form.Control.Feedback type="invalid">
                                {errors.password}
                            </Form.Control.Feedback>
                        </Col>
                    </Form.Group>

                    <Button type="submit">{t('Sign in')}</Button>

                    <Row>
                        <Col sm={'5'}><hr/></Col>
                        <Col sm={'2'}>{t('or connect with')}</Col>
                        <Col sm={'5'}><hr/></Col>
                    </Row>

                    <a href={"http://localhost:8080/oauth2/authorization/google"}>
                        <FontAwesomeIcon icon={faGoogle}/> Google
                    </a>

                </Form>
            )}
        </Formik>
    );
}