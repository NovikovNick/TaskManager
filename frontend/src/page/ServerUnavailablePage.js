import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Store from "../store/ReduxActions";
import {Container} from "react-bootstrap";
import Jumbotron from "react-bootstrap/Jumbotron";
import {useTranslation} from "react-i18next";
import Authenticator from "../container/Authenticator";


function ServerUnavailablePanel() {

    const {t} = useTranslation();

    return (
        <Authenticator path={"/"}>
            <Container className={"h-100 d-flex"}>
                <Jumbotron className={"my-auto w-100 text-center"}>
                    <h1>{t("Unavailable server")}</h1>
                    <p>{t("Sorry, server is not available")}</p>
                </Jumbotron>
            </Container>
        </Authenticator>

    );
}

class ServerUnavailablePage extends Component {
    render() {
        return (<ServerUnavailablePanel/>);
    }
}

const mapStateToProps = state => ({
    user: state.task.user
});

const mapDispatchToProps = (dispatch) => ({
    actions: bindActionCreators(Store, dispatch)
})

export default connect(mapStateToProps, mapDispatchToProps)(ServerUnavailablePage);