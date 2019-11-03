import * as types from '../ActionTypes';

const initialState = {
    taskList: [],
    chess: [4, 2],
};

export default function task(state = initialState, action) {
    switch (action.type) {

        case types.MOVE_KNIGHT: {

            return {
                ...state,
                chess: action.chess
            }
        }

        case types.SET_TASK_LIST: {
            return {
                ...state,
                taskList: action.taskList
            }
        }

        default:
            return state;
    }
}