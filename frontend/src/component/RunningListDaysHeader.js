import React from "react";

import {useTranslation} from "react-i18next";

function Cell({value}) {
    return (
        <div className={"running-list-header-cell text-center"}>
            <div>{value}</div>
        </div>
    );
}

export default function RunningListDaysHeader({calendar}) {

    const {t} = useTranslation()

    return (
        <div className={'running-list-calendar'}>

            {calendar.weekDates.map((day, i) => <Cell key={i} value={day}/>)}

            <br/>

            {['mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'san'].map((day, i) => <Cell key={i} value={t(day)}/>)}

        </div>
    );
}
