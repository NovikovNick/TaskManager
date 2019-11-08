import React from "react";

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useTranslation} from "react-i18next";
import {faChevronLeft, faChevronRight, faSyncAlt, faPlus} from "@fortawesome/free-solid-svg-icons";

function Cell({value}) {
    return (
        <div className={"running-list-header-cell text-center"}>
            <div>{value}</div>
        </div>
    );
}

function RunningListDaysHeader({calendar}) {

    const {t} = useTranslation()

    return (
        <div className={'running-list-calendar'}>

            {calendar.weekDates.map((day, i) => <Cell key={i} value={day}/>)}

            <br/>

            {['mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'san'].map((day, i) => <Cell key={i} value={t(day)}/>)}

        </div>
    );
}

export default function RunningListHeader({calendar, onLoadTaskList, onOpenCreateTaskForm}) {

    return (
        <div className="metalheart-running-list-header">
            <RunningListDaysHeader calendar={calendar}/>

            <div className={'running-list-title'}>
                <h2>Running List</h2>
            </div>

            <div className={'running-list-controls'}>

                <button className="btn btn-default">
                    <FontAwesomeIcon icon={faChevronLeft}/>
                </button>

                <button className="btn btn-default">
                    <FontAwesomeIcon icon={faChevronRight}/>
                </button>


                <button
                    className="btn btn-default"
                    onClick={onLoadTaskList}>
                    <FontAwesomeIcon icon={faSyncAlt}/>
                </button>

                <button
                    className="btn btn-default"
                    onClick={onOpenCreateTaskForm}>
                    <FontAwesomeIcon icon={faPlus}/>
                </button>

            </div>
        </div>
    );
}
