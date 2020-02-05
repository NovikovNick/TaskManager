import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Formik} from 'formik';
import {Button, Form, Modal, Row} from 'react-bootstrap';
import WeekerPicker from './WeekerPicker'


export default function ArchiveModalForm({schema}) {

    const {t} = useTranslation();

    const [errors, setErrors] = useState({});

    const onSubmit = (values, {resetForm}) => {

        schema.onSubmit(values.weekId)
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
                            <Modal.Title>{t("Archive form")}</Modal.Title>
                        </Modal.Header>

                        <Modal.Body>

                            <Form.Group as={Row} controlId="validationFormik00">

                                <WeekerPicker name="weekId"
                                              isInvalid={!!errors.week}
                                              onChange={v => {
                                                  setErrors({})
                                                  handleChange(v)
                                              }}/>

                                <div className={"invalid-feedback"}>{errors.week}</div>

                            </Form.Group>

                        </Modal.Body>
                        <Modal.Footer>
                            <Button type="submit">{t("Save")}</Button>
                        </Modal.Footer>
                    </Form>
                </Modal>
            )}
        </Formik>
    );
}
