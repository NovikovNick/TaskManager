import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPen, faTrash} from "@fortawesome/free-solid-svg-icons";
import React from "react";
import {useTranslation} from "react-i18next";

export default function RunningListItem({index, task, handleRemove, changeStatus, changeTaskTitle, handleEdit}) {

    const {t} = useTranslation()

    const changeText = (e) => {
        task.title = e.target.value;
        changeTaskTitle(task)
    }

    const handleEditFunction = (e) => {
        task.content = e.target.value;
        handleEdit(task)
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

                <input type={"text"} onChange={changeText} value={task.title}></input>
                <span>
                     <span className="pl-1">
                         <FontAwesomeIcon onClick={handleEditFunction} icon={faPen}/>
                     </span>
                     <span className="pl-1">
                         <FontAwesomeIcon onClick={e => handleRemove(task)} icon={faTrash}/>
                     </span>
                </span>
            </div>
        </div>
    );
}