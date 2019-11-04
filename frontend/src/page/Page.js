import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Store from "../store/ReduxActions";

import RunningList from "../container/RunningList";

class Page extends Component {
    render() {

        return (
            <div className="metalheart-wrapper">
                <RunningList/>
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

export default connect(mapStateToProps, mapDispatchToProps)(Page);