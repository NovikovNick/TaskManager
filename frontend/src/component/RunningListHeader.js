import React from "react";

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useTranslation} from "react-i18next";
import {faDownload, faPlus} from "@fortawesome/free-solid-svg-icons";

function Cell({value}) {
    return (
        <div className={"running-list-header-cell text-center"}>
            <div>{value}</div>
        </div>
    );
}

function RunningListDaysHeader() {

    const {t} = useTranslation()

    return (
        <div className={'running-list-calendar'}>

            <Cell value={"04"}/>
            <Cell value={"05"}/>
            <Cell value={"06"}/>
            <Cell value={"07"}/>
            <Cell value={"08"}/>
            <Cell value={"09"}/>
            <Cell value={"10"}/>

            <br/>

            <Cell value={t('mon')}/>
            <Cell value={t('tue')}/>
            <Cell value={t('wed')}/>
            <Cell value={t('thu')}/>
            <Cell value={t('fri')}/>
            <Cell value={t('sat')}/>
            <Cell value={t('san')}/>

        </div>
    );
}

export default function RunningListHeader({onLoadTaskList, onOpenCreateTaskForm}) {

    return (
        <div>
            <div className="metalheart-running-list-header">
                <RunningListDaysHeader/>

                <div className={'running-list-title'}>
                    <h2>Running List</h2>
                </div>

                <div className={'running-list-controls'}>

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

    );
}