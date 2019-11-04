import React from "react";

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useTranslation} from "react-i18next";
import {faDownload, faPlus} from "@fortawesome/free-solid-svg-icons";

function Cell({value}) {
    return (
        <div className={"running-list-cell"}>
            <div>{value}</div>
        </div>
    );
}

function RunningListDaysHeader() {

    const {t} = useTranslation()

    return (
        <div>
            <div className="running-list-row">
                <Cell value={"04"}/>
                <Cell value={"05"}/>
                <Cell value={"06"}/>
                <Cell value={"07"}/>
                <Cell value={"08"}/>
                <Cell value={"09"}/>
                <Cell value={"10"}/>
                <div className="running-list-task">&nbsp;</div>
            </div>
            <div className="running-list-row">
                <Cell value={t('mon')}/>
                <Cell value={t('tue')}/>
                <Cell value={t('wed')}/>
                <Cell value={t('thu')}/>
                <Cell value={t('fri')}/>
                <Cell value={t('sat')}/>
                <Cell value={t('san')}/>
                <div className="running-list-task">&nbsp;</div>
            </div>
        </div>
    );
}

export default function RunningListHeader({onLoadTaskList, onOpenCreateTaskForm}) {

    return (
        <div>
            <div className="metalheart-running-list-header text-center">
                <div className="row">

                    <div className="col-9">
                        <h2>Running List</h2>
                    </div>

                    <div className="col-3">

                        <button
                            className="btn btn-default"
                            onClick={onLoadTaskList}>
                            <FontAwesomeIcon icon={faDownload}/>
                        </button>

                        <button
                            className="btn btn-default"
                            onClick={onOpenCreateTaskForm}>
                            <FontAwesomeIcon icon={faPlus}/>
                        </button>

                    </div>
                </div>
            </div>
            <RunningListDaysHeader/>
        </div>

    );
}