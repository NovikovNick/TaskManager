import * as types from './ActionTypes';
import * as Service from "../service/service";

export const setRunningList = (runningList) => ({type: types.SET_RUNNING_LIST, runningList : runningList});
export const setUser = (user) => ({type: types.SET_USER, user : user});
export const setArchives = (archives) => ({type: types.SET_ARCHIVES, archives : archives});


export function getPrevTaskList(year, week) {
    return (dispatch) => {
        return Service.getPrevTaskList(year, week).then(res => dispatch(setRunningList(res)));
    };
}

export function getNextTaskList(year, week) {
    return (dispatch) => {
        return Service.getNextTaskList(year, week).then(res => dispatch(setRunningList(res)));
    };
}

export function getTaskList() {
    return (dispatch) => {
        return Service.getTaskList().then(res => dispatch(setRunningList(res)));
    };
}

export function createTask(task = {}) {
    return (dispatch) => {
        return Service.createTask(task).then(res => dispatch(setRunningList(res)))
    };
}

export function updateTask(task = {}) {
    return (dispatch) => {
        return Service.updateTask(task).then(res => dispatch(setRunningList(res)))
    };
}

export function archive(weekId = {}) {
    return (dispatch) => {
        return Service.archive(weekId).then(res => {
            dispatch(setUser(res.user));
            dispatch(setRunningList(res.runningList));
            dispatch(setArchives(res.archives));
        })
    };
}

export function selectTag(text) {
    return (dispatch) => {
        return Service.addTag(text).then(res => dispatch(setRunningList(res)))
    };
}

export function unselectTag(text) {
    return (dispatch) => {
        return Service.removeTag(text).then(res => dispatch(setRunningList(res)))
    };
}

export function undo() {
    return (dispatch) => {
        return Service.undo()
            .then(res => {
                dispatch(setUser(res.user));
                dispatch(setRunningList(res.runningList));
                dispatch(setArchives(res.archives));
            })
    };
}

export function redo() {
    return (dispatch) => {
        return Service.redo()
            .then(res => {
                dispatch(setUser(res.user));
                dispatch(setRunningList(res.runningList));
                dispatch(setArchives(res.archives));
            })
    };
}

export function getUserProfile() {
    return (dispatch) => {
        return Service.getUserProfile().then(res => dispatch(setUser(res)))
    };
}

export function saveProfile(request) {
    return (dispatch) => {
        return Service.saveProfile(request)
            .then(res => {
                dispatch(setUser(res.user));
                dispatch(setRunningList(res.runningList));
                dispatch(setArchives(res.archives));
            })
    };
}

export function changePassword(request) {
    return (dispatch) => {
        return Service.changePassword(request);
    };
}

export function sendChangePasswordEmail(request) {
    return (dispatch) => {
        return Service.sendChangePasswordEmail(request);
    };
}

export function signIn(request) {
    return (dispatch) => {
        return Service.signIn(request).then(res => {
            res.user && dispatch(setUser(res.user));
            res.runningList && dispatch(setRunningList(res.runningList));
            res.archives && dispatch(setArchives(res.archives));
        })
    };
}

export function signOut(request) {
    return (dispatch) => {
        return Service.signOut(request);
    };
}

export function signUp(request) {
    return (dispatch) => {
        return Service.signUp(request);
    };
}

export function changePriority(startIndex, endIndex) {
    return (dispatch) => {
        return Service.changePriority(startIndex, endIndex).then(res => dispatch(setRunningList(res)))
    };
}

export function deleteTask(task) {
    return (dispatch) => {
        return Service.deleteTask(task).then(res => dispatch(setRunningList(res)))
    };
}

export function changeTaskStatus(taskid, status, dayIndex) {
    return (dispatch) => {
        return Service.changeTaskStatus(taskid, status, dayIndex).then(res => dispatch(setRunningList(res)))
    };
}

export function getExistingArchivesWeekIds() {
    return (dispatch) => {
        return Service.getExistingArchivesWeekIds().then(res => dispatch(setArchives(res)))
    };
}