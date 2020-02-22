import React from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronLeft} from "@fortawesome/free-solid-svg-icons";
import Button from "react-bootstrap/Button";
import * as Store from "../../store/ReduxActions";


function NextArchiveHeaderControl({actions, runningList}) {

    const nextArchive = () => {
        const {year, week} = runningList;
        actions.getNextTaskList(year, week);
    }

    return (
        <Button variant="outline-light" disabled={!runningList.hasNext} onClick={nextArchive}>
            <FontAwesomeIcon icon={faChevronLeft}/>
        </Button>
    );
}

const mapStateToProps = state => ({
    runningList: state.task.runningList
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(NextArchiveHeaderControl);