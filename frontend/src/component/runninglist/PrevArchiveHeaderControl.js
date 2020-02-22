import React from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronRight} from "@fortawesome/free-solid-svg-icons";
import Button from "react-bootstrap/Button";
import * as Store from "../../store/ReduxActions";
import PropTypes from "prop-types";


function PrevArchiveHeaderControl({actions, runningList}) {

    const prevArchive = () => {
        const {year, week} = runningList;
        actions.getPrevTaskList(year, week);
    }

    return (
        <Button variant="outline-light" disabled={!runningList.hasPrevious} onClick={prevArchive}>
            <FontAwesomeIcon icon={faChevronRight}/>
        </Button>
    );
}

PrevArchiveHeaderControl.propTypes = {
    actions: PropTypes.object.isRequired,
    runningList: PropTypes.shape({
        calendar: PropTypes.object.isRequired,
        tasks: PropTypes.array.isRequired,
        selectedTags: PropTypes.array.isRequired,
        allTags: PropTypes.array.isRequired
    })
};

const mapStateToProps = state => ({
    runningList: state.task.runningList
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(PrevArchiveHeaderControl);