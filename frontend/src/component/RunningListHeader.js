import React from "react";

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useTranslation} from "react-i18next";
import {faChevronLeft, faChevronRight, faSyncAlt, faPlus, faSave, faUndo, faRedo} from "@fortawesome/free-solid-svg-icons";
import Button from "react-bootstrap/Button";

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

export default function RunningListHeader({
                                              calendar,
                                              onLoadTaskList,
                                              onOpenCreateTaskForm,
                                              canUndo, canRedo, canArchive, hasNext, hasPrev,
                                              onUndo, onRedo, onArchive, onNext, onPrev
                                          }) {

    return (
        <div className="metalheart-running-list-header">
            <RunningListDaysHeader calendar={calendar}/>

            <div className={'running-list-title'}>
                <h2>Running List</h2>
            </div>

            <div className={'running-list-controls'}>

                <Button variant="outline-light" disabled={!canUndo} onClick={onUndo}>
                    <FontAwesomeIcon icon={faUndo}/>
                </Button>

                <Button variant="outline-light" disabled={!canRedo} onClick={onRedo}>
                    <FontAwesomeIcon icon={faRedo}/>
                </Button>

                <Button variant="outline-light" disabled={!canArchive} onClick={onArchive}>
                    <FontAwesomeIcon icon={faSave}/>
                </Button>

                <Button variant="outline-light" disabled={!hasNext} onClick={onNext}>
                    <FontAwesomeIcon icon={faChevronLeft}/>
                </Button>

                <Button variant="outline-light" disabled={!hasPrev} onClick={onPrev}>
                    <FontAwesomeIcon icon={faChevronRight}/>
                </Button>

                <Button variant="outline-light" onClick={onLoadTaskList}>
                    <FontAwesomeIcon icon={faSyncAlt}/>
                </Button>

                <Button variant="outline-light" onClick={onOpenCreateTaskForm}>
                    <FontAwesomeIcon icon={faPlus}/>
                </Button>

            </div>
        </div>
    );
}
