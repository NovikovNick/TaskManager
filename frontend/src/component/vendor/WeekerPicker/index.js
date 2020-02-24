import React, {useState} from 'react';
import moment from 'moment';
import DayPicker from 'react-day-picker';
import 'react-day-picker/lib/style.css';
import './styles.scss';

import MomentLocaleUtils from 'react-day-picker/moment';
import 'moment/locale/ru';


function getWeekDays(weekStart) {
    const days = [weekStart];
    for (let i = 1; i < 7; i += 1) {
        days.push(
            moment(weekStart)
                .add(i, 'days')
                .toDate()
        );
    }
    return days;
}

function getWeekRange(date) {
    return {
        from: moment(date)
            .startOf('week')
            .toDate(),
        to: moment(date)
            .endOf('week')
            .toDate(),
    };
}

function getWeekRangeByWeekNumber(year, week) {
    return {
        from: moment(year + "-" + week, 'YYYY-w')
            .startOf('week')
            .toDate(),
        to: moment(year + "-" + week, 'YYYY-w')
            .endOf('week')
            .toDate(),
    };
}

function WeekPicker({isInvalid, name, onChange, archives}) {

    const [hoverRange, setHoverRange] = useState(undefined);
    const [selectedDays, setSelectedDays] = useState([]);


    const handleDayChange = date => {

        onChange({
            target: {
                name: name,
                value: {
                    week: moment(date).week(),
                    year: moment(date).year(),
                }
            }
        });

        setSelectedDays(getWeekDays(getWeekRange(date).from));
    };

    const handleDayEnter = date => {
        setHoverRange(getWeekRange(date))
    };

    const handleDayLeave = () => {
        getWeekRange(undefined)
    };

    const handleWeekClick = (weekNumber, days, e) => {
        setSelectedDays(days);
    };

    const daysAreSelected = selectedDays.length > 0;

    const existedArchives = archives.map((weekId) => {
        return getWeekRangeByWeekNumber(weekId.year, weekId.week)
    });

    const modifiers = {
        hoverRange,
        selectedRange: daysAreSelected && {
            from: selectedDays[0],
            to: selectedDays[6],
        },
        existedArchives,
        hoverRangeStart: hoverRange && hoverRange.from,
        hoverRangeEnd: hoverRange && hoverRange.to,
        selectedRangeStart: daysAreSelected && selectedDays[0],
        selectedRangeEnd: daysAreSelected && selectedDays[6]
    };

    return (
        <div className={"weeker_picker_wrapper " + (isInvalid ? " is-invalid" : "")}>
            <DayPicker

                locale={navigator.language || navigator.userLanguage}
                localeUtils={MomentLocaleUtils}

                selectedDays={selectedDays}
                showWeekNumbers
                showOutsideDays
                modifiers={modifiers}
                onDayClick={handleDayChange}
                onDayMouseEnter={handleDayEnter}
                onDayMouseLeave={handleDayLeave}
                onWeekClick={handleWeekClick}
            />
            {selectedDays.length === 7 && (
                <div className={"text-center"}>
                    {moment(selectedDays[0]).format('LL')}{' '}–{' '}{moment(selectedDays[6]).format('LL')}
                </div>
            )}
        </div>
    );
}

export default WeekPicker;