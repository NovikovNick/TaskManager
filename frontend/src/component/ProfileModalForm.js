import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Formik} from 'formik';
import {Button, Col, Form, Modal, Row} from 'react-bootstrap';
import {WithContext as ReactTags} from "react-tag-input";


export default function ProfileModalForm({schema}) {

    const {t} = useTranslation();

    const [errors, setErrors] = useState({});

    const onSubmit = (values, {resetForm}) => {

        schema.onSubmit(values.tags)
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

                        <Modal.Body>

                            <Form.Group as={Row} controlId="validationFormik00">
                                <Form.Label column sm="3">{t('tags')}</Form.Label>
                                <Col sm={'9'}>
                                    <ReactTags tags={values.tags || []}
                                               placeholder={t("tags")}
                                               suggestions={values.tagsSuggestion || []}
                                               handleDelete={(i) => setFieldValue('tags', values.tags.filter((tag, index) => index !== i))}
                                               handleAddition={(tag) => setFieldValue('tags', [...values.tags, tag])}/>

                                    <div className={"invalid-feedback"}>{errors.tags}</div>
                                </Col>
                            </Form.Group>

                        </Modal.Body>
                        <Modal.Footer>
                            <Button onClick={() => window.location = "/changepassword"}>
                                {t("Send email to change password")}
                            </Button>
                            <Button type="submit">{t("Save")}</Button>
                        </Modal.Footer>
                    </Form>
                </Modal>
            )}
        </Formik>
    );
}
