import React from "react";

import {useTranslation} from "react-i18next";
import HeaderCell from "./HeaderCell";
import {connect} from "react-redux";
import PropTypes from "prop-types";


function Days({calendar}) {

    const {t} = useTranslation()

    return (
        <div className={'running-list-calendar'}>

            {calendar.weekDates.map((day, i) => <HeaderCell key={i} value={day}/>)}

            <br/>

            {['mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'san'].map((day, i) => <HeaderCell key={i} value={t(day)}/>)}

        </div>
    );
}

Days.propTypes = {
    calendar: PropTypes.object.isRequired,
};

const mapStateToProps = state => ({
    calendar: state.task.runningList.calendar
});
export default connect(mapStateToProps)(Days);