import * as types from '../ActionTypes';
import React from "react";

const initialState = {
    runningList: {
        calendar: {
            currentDay: "",
            weekDates: []
        },
        tasks: [],
        selectedTags: [],
        allTags: []
    }
};

export default function task(state = initialState, action) {
    switch (action.type) {

        case types.SET_RUNNING_LIST: {
            return {
                ...state,
                runningList: action.runningList
            }
        }

        default:
            return state;
    }
}