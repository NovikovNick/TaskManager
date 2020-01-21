import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Store from "../store/ReduxActions";
import LoginRegistration from "../container/LoginRegistration";

class PageLogin extends Component {
    render() {
        return (<LoginRegistration/>);
    }
}

const mapStateToProps = state => ({
    taskList: state.task.taskList
});

const mapDispatchToProps = (dispatch) => ({
    actions: bindActionCreators(Store, dispatch)
})

export default connect(mapStateToProps, mapDispatchToProps)(PageLogin);