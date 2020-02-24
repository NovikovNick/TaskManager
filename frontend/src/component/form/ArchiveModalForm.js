import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';

import {useTranslation} from "react-i18next";
import {Formik} from 'formik';
import {Button, Form, Modal, Row} from 'react-bootstrap';
import WeekerPicker from '../vendor/WeekerPicker/index'
import * as Store from "../../store/ReduxActions";


function ArchiveModalForm({isActive, toggle, actions, archives}) {

    const {t} = useTranslation();
    const [errors, setErrors] = useState({});

    useEffect(() => {
        actions.getExistingArchivesWeekIds();
    }, []);

    const onSubmit = (values, {resetForm}) => {

        actions.archive(values.weekId)
            .then(res => {
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
                                              archives={archives}
                                              isInvalid={!!errors.year || !!errors.week}
                                              onChange={v => {
                                                  setErrors({})
                                                  handleChange(v)
                                              }}/>

                                <div className={"invalid-feedback"}>{errors.year || errors.week}</div>

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

ArchiveModalForm.propTypes = {
    isActive: PropTypes.bool.isRequired,
    toggle: PropTypes.func.isRequired,
    actions: PropTypes.object.isRequired,
    archives: PropTypes.array.isRequired
};


const mapStateToProps = state => ({
    archives: state.task.archives
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});
export default connect(mapStateToProps, mapDispatchToProps)(ArchiveModalForm);
