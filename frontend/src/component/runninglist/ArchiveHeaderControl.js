import React from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faSave} from "@fortawesome/free-solid-svg-icons";
import Button from "react-bootstrap/Button";
import useModal from "../../hook/useModal";
import ArchiveModalForm from "../form/ArchiveModalForm";
import {connect} from "react-redux";
import PropTypes from "prop-types";


function ArchiveHeaderControl({runningList}) {

    const {isActive, toggle} = useModal();

    return (
        <span>
            <Button variant="outline-light" disabled={!runningList.editable} onClick={toggle}>
                <FontAwesomeIcon icon={faSave}/>
            </Button>
            <ArchiveModalForm isActive={isActive} toggle={toggle}/>
        </span>

    );
}

ArchiveHeaderControl.propTypes = {
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

export default connect(mapStateToProps)(ArchiveHeaderControl);