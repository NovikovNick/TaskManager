import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Store from "../store/ReduxActions";
import LoginForm from "../component/LoginForm";
import * as REST from "../rest/rest";

import {Container} from 'react-bootstrap';

class PageLogin extends Component {
    render() {

        return (
            <div className="metalheart-wrapper">
                <Container>
                    <LoginForm schema={{
                        formData: {username: '', password: ''},
                        onSubmit: REST.signIn,
                        onSuccess: () => { window.location = "/"}
                    }}/>
                </Container>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    taskList: state.task.taskList
});

const mapDispatchToProps = (dispatch) => ({
    actions: bindActionCreators(Store, dispatch)
})

export default connect(mapStateToProps, mapDispatchToProps)(PageLogin);