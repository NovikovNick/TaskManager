import * as types from './ActionTypes';

export const setTaskList = (taskList) => ({type: types.SET_TASK_LIST, taskList : taskList});

export const moveKnight = (x, y) => ({type: types.MOVE_KNIGHT, chess : [x, y]});

