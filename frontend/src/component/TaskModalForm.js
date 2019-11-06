import React from 'react';
import {useTranslation} from "react-i18next";
import {Formik} from 'formik';
import {Button, Col, Form, Modal, Row} from 'react-bootstrap';


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

export function TaskModalForm({id, schema}) {

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
                  handleSubmit,
                  values,
                  errors,
                  touched,
                  resetForm
              }) => (

                <Modal show={schema.uiSchema.active} onHide={() => {
                    resetForm({})
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
                                        name="title"
                                        onChange={handleChange}
                                        defaultValue={values.title}
                                        isValid={touched.title && !errors.title}
                                        isInvalid={!!errors.title}
                                    />
                                    <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
                                    <Form.Control.Feedback type="invalid">
                                        {errors.title}
                                    </Form.Control.Feedback>
                                </Col>
                            </Form.Group>

                            <Form.Group as={Row} controlId="validationFormik02">
                                <Form.Label column sm="3">{t('description')}</Form.Label>
                                <Col sm={'9'}>
                                    <Form.Control
                                        as="textarea"
                                        name="description"
                                        onChange={handleChange}
                                        defaultValue={values.description}
                                        isValid={touched.description && !errors.description}
                                        isInvalid={!!errors.description}
                                    />
                                    <Form.Control.Feedback type="valid">{t('Looks good!')}</Form.Control.Feedback>
                                    <Form.Control.Feedback type="invalid">
                                        {errors.description}
                                    </Form.Control.Feedback>
                                </Col>
                            </Form.Group>

                        </Modal.Body>
                        <Modal.Footer>
                            <Button type="submit">{t('Create task')}</Button>
                        </Modal.Footer>
                    </Form>
                </Modal>
            )}
        </Formik>
    );
}