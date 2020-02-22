import React, {useEffect} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faRedo} from "@fortawesome/free-solid-svg-icons";
import Button from "react-bootstrap/Button";
import KeyCodes from "../../KeyCodes";
import * as Store from "../../store/ReduxActions";


function RedoHeaderControl({actions, runningList}) {

    const redo = () => {
        actions.redo();
    }

    useEffect(() => {

        const ctrlShiftZ = (e) => {

            if (e.keyCode === KeyCodes.Z && (e.ctrlKey && e.shiftKey) && runningList.canRedo) {
                redo();
            }
        }

        document.addEventListener('keydown', ctrlShiftZ);

        return () => {
            document.removeEventListener('keydown', ctrlShiftZ);
        }
    }, [runningList.canRedo]);


    return (
        <Button variant="outline-light" disabled={!runningList.canRedo} onClick={redo}>
            <FontAwesomeIcon icon={faRedo}/>
        </Button>
    );
}

const mapStateToProps = state => ({
    runningList: state.task.runningList
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(RedoHeaderControl);