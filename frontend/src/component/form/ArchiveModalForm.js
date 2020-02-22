import React, {useState} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import {useTranslation} from "react-i18next";
import {Formik} from 'formik';
import {Button, Form, Modal, Row} from 'react-bootstrap';
import WeekerPicker from '../vendor/WeekerPicker'
import * as Service from "../../service/service";
import * as Store from "../../store/ReduxActions";


function ArchiveModalForm({isActive, toggle, actions}) {

    const {t} = useTranslation();
    const [errors, setErrors] = useState({});

    const onSubmit = (values, {resetForm}) => {

        Service.archive(values.weekId)
            .then(res => {
                Service.getTaskList().then(actions.setRunningList);
                resetForm({});
                toggle();
            })
            .catch(setErrors);
    };

    return (
        <Formik
            enableReinitialize
            initialValues={{}}
            onSubmit={onSubmit}>

            {({
                  handleChange,
                  setFieldValue,
                  handleSubmit,
                  values,
                  touched,
                  resetForm
              }) => (

                <Modal show={isActive} onHide={() => {
                    resetForm({});
                    toggle();
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

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});
export default connect(null, mapDispatchToProps)(ArchiveModalForm);
