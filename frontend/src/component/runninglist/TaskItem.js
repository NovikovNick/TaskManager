import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import {useTranslation} from "react-i18next";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPen, faTrash} from "@fortawesome/free-solid-svg-icons";
import * as Store from "../../store/ReduxActions";
import PropTypes from "prop-types";

function TaskItem({task, actions, handleEdit}) {

    const {t} = useTranslation()

    const changeText = (e) => {
        const newTask = {...task};
        newTask.title = e.target.value;
        actions.updateTask(newTask)
    }

    const handleRemove = (task) => {
        actions.deleteTask(task.id);
    }

    const changeStatus = (task, status, dayIndex) => {
        actions.changeTaskStatus(task.id, status, dayIndex);
    }

    const key = task.id

    return (
        <div className="running-list-row">

            {
                task.status.map((status, dayIndex) =>
                    <div key={key + '_' + dayIndex} index={dayIndex} className={"running-list-cell " + status}>
                        <div className="dropdown show line-through">
                            <div role="button"
                                 id={key + "_dropdownMenuLink_" + dayIndex} data-toggle="dropdown"
                                 aria-haspopup="true"
                                 aria-expanded="false">
                                &nbsp;
                            </div>

                            <div className="dropdown-menu" aria-labelledby="dropdownMenuLink">
                                <button className="dropdown-item"
                                        onClick={(e) => changeStatus(task, "NONE", dayIndex)}>
                                    {t('NONE')}
                                </button>
                                <button className="dropdown-item"
                                        onClick={(e) => changeStatus(task, "TO_DO", dayIndex)}>
                                    {t('TO_DO')}
                                </button>
                                <button className="dropdown-item"
                                        onClick={(e) => changeStatus(task, "IN_PROGRESS", dayIndex)}>
                                    {t('IN_PROGRESS')}
                                </button>
                                <button className="dropdown-item"
                                        onClick={(e) => changeStatus(task, "CANCELED", dayIndex)}>
                                    {t('CANCELED')}
                                </button>
                                <button className="dropdown-item"
                                        onClick={(e) => changeStatus(task, "DONE", dayIndex)}>
                                    {t('DONE')}
                                </button>
                            </div>
                        </div>
                    </div>
                )
            }

            <div className="running-list-task">

                <div className="running-list-task-input">
                    <input type={"text"} onChange={changeText} value={task.title}></input>
                </div>

                <div className={"running-list-tags"}>
                    {
                        task.tags && task.tags.map((tag, i) => <div className={"running-list-tag"} key={i}>{tag.text}</div>)
                    }
                </div>

                <div className={"running-list-controls"}>
                     <span className="pl-1">
                         <FontAwesomeIcon onClick={e => handleEdit(task)} icon={faPen}/>
                     </span>
                    <span className="pl-1">
                         <FontAwesomeIcon onClick={e => handleRemove(task)} icon={faTrash}/>
                     </span>
                </div>

            </div>
        </div>
    );
}

TaskItem.propTypes = {
    handleEdit: PropTypes.func.isRequired,
    actions: PropTypes.object.isRequired,
    task: PropTypes.shape({
        title: PropTypes.string.isRequired,
        description: PropTypes.string,
        tags: PropTypes.array
    })
};

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});
export default connect(null, mapDispatchToProps)(TaskItem);