import * as types from '../ActionTypes';
import React from "react";

const initialState = {
    user: {
        id: null,
        username: null,
    },
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
        case types.SET_USER: {
            return {
                ...state,
                user: action.user
            }
        }
        default:
            return state;
    }
}