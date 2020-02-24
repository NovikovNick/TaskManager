import * as types from '../ActionTypes';

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
    },
    archives: []
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
        case types.SET_ARCHIVES: {
            return {
                ...state,
                archives: action.archives
            }
        }
        default:
            return state;
    }
}