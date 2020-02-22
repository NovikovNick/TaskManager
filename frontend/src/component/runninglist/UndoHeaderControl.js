import React, {useEffect} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUndo} from "@fortawesome/free-solid-svg-icons";
import Button from "react-bootstrap/Button";
import KeyCodes from "../../KeyCodes";
import * as Store from "../../store/ReduxActions";
import PropTypes from "prop-types";


function UndoHeaderControl({actions, runningList}) {

    const undo = () => {
        actions.undo();
    }

    useEffect(() => {

        const ctrlZ = (e) => {
            if (e.keyCode === KeyCodes.Z && (e.ctrlKey && !e.shiftKey) && runningList.canUndo){
                undo();
            }
        }

        document.addEventListener('keydown', ctrlZ);

        return () => {
            document.removeEventListener('keydown', ctrlZ);
        }
    }, [runningList.canUndo]);

    return (
        <Button variant="outline-light" disabled={!runningList.canUndo} onClick={undo}>
            <FontAwesomeIcon icon={faUndo}/>
        </Button>
    );
}

UndoHeaderControl.propTypes = {
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

export default connect(mapStateToProps, mapDispatchToProps)(UndoHeaderControl);