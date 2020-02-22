import React from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronRight} from "@fortawesome/free-solid-svg-icons";
import Button from "react-bootstrap/Button";
import * as Service from "../../service/service";
import * as Store from "../../store/ReduxActions";


function PrevArchiveHeaderControl({actions, runningList}) {

    const prevArchive = () => {
        const {year, week} = runningList;
        Service.getPrevTaskList(year, week).then(actions.setRunningList);
    }

    return (
        <Button variant="outline-light" disabled={!runningList.hasPrevious} onClick={prevArchive}>
            <FontAwesomeIcon icon={faChevronRight}/>
        </Button>
    );
}

const mapStateToProps = state => ({
    runningList: state.task.runningList
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(PrevArchiveHeaderControl);