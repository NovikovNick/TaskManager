import React from 'react';
import moment from 'moment';
import DayPicker from 'react-day-picker';
import 'react-day-picker/lib/style.css';

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

export default class WeekPicker extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            hoverRange: undefined,
            selectedDays: [],
            ...props
        };
    }

    handleDayChange = date => {


        this.state.onChange({
            target: {
                name: this.state.name,
                value: {
                    week: moment(date).week(),
                    year: moment(date).year(),
                }
            }
        })
        this.setState({
            selectedDays: getWeekDays(getWeekRange(date).from),
        });
    };

    handleDayEnter = date => {
        this.setState({
            hoverRange: getWeekRange(date),
        });
    };

    handleDayLeave = () => {
        this.setState({
            hoverRange: undefined,
        });
    };

    handleWeekClick = (weekNumber, days, e) => {
        this.setState({
            selectedDays: days,
        });
    };

    render() {

        const {hoverRange, selectedDays} = this.state;
        const {isInvalid} = this.props;

        const daysAreSelected = selectedDays.length > 0;

        const modifiers = {
            hoverRange,
            selectedRange: daysAreSelected && {
                from: selectedDays[0],
                to: selectedDays[6],
            },
            hoverRangeStart: hoverRange && hoverRange.from,
            hoverRangeEnd: hoverRange && hoverRange.to,
            selectedRangeStart: daysAreSelected && selectedDays[0],
            selectedRangeEnd: daysAreSelected && selectedDays[6],
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
                    onDayClick={this.handleDayChange}
                    onDayMouseEnter={this.handleDayEnter}
                    onDayMouseLeave={this.handleDayLeave}
                    onWeekClick={this.handleWeekClick}
                />
                {selectedDays.length === 7 && (
                    <div className={"text-center"}>
                        {moment(selectedDays[0]).format('LL')}{' '}–{' '}{moment(selectedDays[6]).format('LL')}
                    </div>
                )}
            </div>
        );
    }
}