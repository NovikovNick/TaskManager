import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Store from "../store/ReduxActions";

import Chess from "../container/Chess";
import RunningList from "../container/RunningList";
import List from "../container/List";

class Page extends Component {
    render() {

        return (
            <div className="metalheart-wrapper">
                {/*<Chess/>*/}
                <RunningList/>
                {/*<List/>*/}
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