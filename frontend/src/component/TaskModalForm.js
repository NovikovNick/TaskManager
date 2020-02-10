import React, {useRef, useState} from 'react';
import {useTranslation} from "react-i18next";
import {Formik} from 'formik';
import {Button, Col, Form, Modal, Row} from 'react-bootstrap';
import {FormTags} from "./Tags";


export function UpdateTaskModalForm({schema}) {

    schema.uiSchema.lang = {
        title: 'update_task_title',
        submit: 'update_task_submit_btn',
    }

    return (<TaskModalForm schema={schema}/>);
}

export function CreateTaskModalForm({schema}) {
    schema.uiSchema.lang = {
        title: 'create_task_title',
        submit: 'create_task_submit_btn',
    }

    return (<TaskModalForm schema={schema}/>);
}

export function TaskModalForm({schema}) {

    const {t} = useTranslation();

    const [errors, setErrors] = useState({});
    const [valid, setValid] = useState({});
    const titleInput = useRef();

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

                <Modal show={schema.uiSchema.active}
                       onShow={() => titleInput.current.focus()}
                       onHide={() => {
                           resetForm({});
                           schema.closeForm();
                       }}>
                    <Form noValidate onSubmit={handleSubmit}>

                        <Modal.Header closeButton>
                            <Modal.Title>{t(schema.uiSchema.lang.title)}</Modal.Title>
                        </Modal.Header>

                        <Modal.Body>

                            <Form.Group as={Row} controlId="validationFormik01">
                                <Form.Label column sm="3">{t('title')}</Form.Label>
                                <Col sm={'9'}>
                                    <Form.Control
                                        ref={titleInput}
                                        name="title"
                                        onChange={(v) => {
                                            if (errors && errors.title) {
                                                const {title, ...rest} = errors;
                                                setErrors(rest);
                                            }
                                            if (valid && valid.title) {
                                                const {title, ...rest} = valid;
                                                setValid(rest);
                                            }
                                            handleChange(v);
                                        }}
                                        defaultValue={values.title}
                                        isValid={touched.title && valid.title}
                                        isInvalid={!!errors.title}
                                    />
                                    <Form.Control.Feedback type="invalid">{errors.title}</Form.Control.Feedback>
                                </Col>
                            </Form.Group>

                            <Form.Group as={Row} controlId="validationFormik02">
                                <Form.Label column sm="3">{t('description')}</Form.Label>
                                <Col sm={'9'}>
                                    <Form.Control
                                        as="textarea"
                                        name="description"
                                        onChange={(v) => {
                                            if (errors && errors.description) {
                                                const {description, ...rest} = errors;
                                                setErrors(rest);
                                            }
                                            if (valid && valid.description) {
                                                const {description, ...rest} = valid;
                                                setValid(rest);
                                            }
                                            handleChange(v);
                                        }}
                                        defaultValue={values.description}
                                        isValid={touched.description && valid.description}
                                        isInvalid={!!errors.description}
                                    />
                                    <Form.Control.Feedback type="invalid">{errors.description}</Form.Control.Feedback>
                                </Col>
                            </Form.Group>

                            <Form.Group as={Row} controlId="validationFormik03">
                                <Form.Label column sm="3">{t('tags')}</Form.Label>
                                <Col sm={'9'}>
                                    <FormTags tags={values.tags || []}
                                               placeholder={t("Add tags")}
                                               suggestions={values.tagsSuggestion || []}
                                               handleDelete={(i) => setFieldValue('tags', values.tags.filter((tag, index) => index !== i))}
                                               handleAddition={(tag) => setFieldValue('tags', [...values.tags, tag])}/>
                                    <div className={"invalid-feedback"}>{errors.tags}</div>
                                </Col>
                            </Form.Group>

                        </Modal.Body>
                        <Modal.Footer>
                            <Button type="submit">{t(schema.uiSchema.lang.submit)}</Button>
                        </Modal.Footer>
                    </Form>
                </Modal>
            )}
        </Formik>
    );
}