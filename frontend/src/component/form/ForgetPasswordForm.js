import React, {useState} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import {useTranslation} from "react-i18next";
import {Button, Form, Row} from 'react-bootstrap';
import {Formik} from "formik";
import * as Store from "../../store/ReduxActions";
import PropTypes from "prop-types";

function ForgetPasswordForm({actions}) {
    const {t} = useTranslation();
    const [errors, setErrors] = useState({});
    const [valid, setValid] = useState({});

    const STATE = {
        FORGET_FORM: "FORGET_FORM",
        EMAIL_SENT: "EMAIL_SENT",
        SERVER_ERROR: "SERVER_ERROR"
    }

    const [state, setState] = useState(STATE.FORGET_FORM);


    const onSubmit = (values, {resetForm}) => {

        actions.sendChangePasswordEmail(values)
            .then(res => {
                resetForm({})
                if (res) {
                    setState(STATE.EMAIL_SENT)
                } else {
                    setState(STATE.SERVER_ERROR)
                }
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

    const success = (<div className={"text-center result-panel"}>
        <h2>{t("Check your email inbox")}</h2>
        <p>{t("We sent an email link to change password")}</p>
    </div>);

    const fail = (<div className={"text-center result-panel"}>
        <h2>{t("Server error")}</h2>
        <p>{t("Email server is not available at the moment")}</p>
    </div>);

    const forgetForm = <Formik
        enableReinitialize
        initialValues={{email: ''}}
        onSubmit={onSubmit}>
        {({
              handleChange,
              handleSubmit,
              values,
              touched
          }) => (

            <Form noValidate onSubmit={handleSubmit}>

                <h2>{t("Change password")}</h2>

                <Form.Group as={Row} controlId="validationFormik01">
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
                        isValid={touched.email && !errors.email}
                        isInvalid={!!errors.email}
                        placeholder={t("Email")}
                    />
                    <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                </Form.Group>

                <Row><Button type="submit">{t('Send email')}</Button></Row>
            </Form>
        )}
    </Formik>;

    switch (state) {
        case STATE.FORGET_FORM:
            return forgetForm;
        case STATE.EMAIL_SENT:
            return success;
        case STATE.SERVER_ERROR:
            return fail;
        default:
            return forgetForm;
    }
}

ForgetPasswordForm.propTypes = {
    actions: PropTypes.object.isRequired
};

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(null, mapDispatchToProps)(ForgetPasswordForm);