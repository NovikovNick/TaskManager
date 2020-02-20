import React from "react";

import {useTranslation} from "react-i18next";
import HeaderCell from "./HeaderCell";


export default function Days({calendar}) {

    const {t} = useTranslation()

    return (
        <div className={'running-list-calendar'}>

            {calendar.weekDates.map((day, i) => <HeaderCell key={i} value={day}/>)}

            <br/>

            {['mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'san'].map((day, i) => <HeaderCell key={i} value={t(day)}/>)}

        </div>
    );
}
