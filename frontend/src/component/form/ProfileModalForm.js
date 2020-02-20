import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Formik} from 'formik';
import {Button, Col, Form, Modal, Row} from 'react-bootstrap';
import {FormTags} from "../vendor/Tags";
import ChangePasswordPageLink from "../link/ChangePasswordPageLink"

export default function ProfileModalForm({schema}) {

    const {t} = useTranslation();

    const [errors, setErrors] = useState({});
    const [valid, setValid] = useState({});

    const onSubmit = (values, {resetForm}) => {

        schema.onSubmit(values)
            .then(res => {
                schema.onSuccess(res);
                resetForm({})
            })
            .catch(response => {
                setErrors(response)

                // if key doesn't present in errors, then it is valid
                var valid = Object.keys(values).reduce(function (obj, k) {
                    if (!response.hasOwnProperty(k)) obj[k] = values[k];
                    return obj;
                }, {});
                setValid(valid);
            });
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
                  touched,
                  resetForm
              }) => (

                <Modal show={schema.uiSchema.active} onHide={() => {
                    resetForm({})
                    schema.closeForm();
                }}>
                    <Form noValidate onSubmit={handleSubmit}>

                        <Modal.Header closeButton>
                            <Modal.Title>{t("Profile")}</Modal.Title>
                        </Modal.Header>

                        <Modal.Body className={"runninglist-profile"}>
                            <Form.Group as={Row} controlId="validationFormik11">
                                <Form.Label column sm="3">{t('Username')}</Form.Label>
                                <Col sm={'9'}>
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
                                </Col>
                            </Form.Group>

                            <Form.Group as={Row} controlId="validationFormik12">
                                <Form.Label column sm="3">{t('Email')}</Form.Label>
                                <Col sm={'9'}>
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
                                </Col>
                            </Form.Group>

                            <Form.Group as={Row} controlId="validationFormik00">
                                <Form.Label column sm="3">{t('tags')}</Form.Label>
                                <Col sm={'9'}>
                                    <FormTags tags={values.tags || []}
                                          placeholder={t("tags")}
                                          suggestions={values.tagsSuggestion || []}
                                          handleDelete={(i) => setFieldValue('tags', values.tags.filter((tag, index) => index !== i))}
                                          handleAddition={(tag) => setFieldValue('tags', [...values.tags, tag])}/>
                                    <div className={"invalid-feedback"}>{errors.tags}</div>
                                </Col>
                            </Form.Group>

                        </Modal.Body>
                        <Modal.Footer>

                            <ChangePasswordPageLink />

                            <Button type="submit">{t("Save")}</Button>

                        </Modal.Footer>
                    </Form>
                </Modal>
            )}
        </Formik>
    );
}