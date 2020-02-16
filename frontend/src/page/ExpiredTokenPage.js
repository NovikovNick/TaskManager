import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Store from "../store/ReduxActions";
import {Container} from "react-bootstrap";
import Jumbotron from "react-bootstrap/Jumbotron";
import Button from "react-bootstrap/Button";
import {useTranslation} from "react-i18next";


function TokenExpiredPanel() {

    const {t} = useTranslation();

    const routeToLoginPage = <Button
        onClick={() => window.location = "/signin"}
        variant="primary">{t("Return to login page")}</Button>

    return (
        <Container className={"h-100 d-flex"}>
            <Jumbotron className={"my-auto w-100 text-center"}>
                <h1>{t("Token expired")}</h1>
                <p>{t("Sorry, your token expired")}</p>
                <p className={"p-4"}>
                    {routeToLoginPage}
                </p>
            </Jumbotron>
        </Container>
    );
}

class ExpiredTokenPage extends Component {
    render() {
        return (<TokenExpiredPanel />);
    }
}

const mapStateToProps = state => ({
    user: state.task.user
});

const mapDispatchToProps = (dispatch) => ({
    actions: bindActionCreators(Store, dispatch)
})

export default connect(mapStateToProps, mapDispatchToProps)(ExpiredTokenPage);