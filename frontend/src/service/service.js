import setting from "../config"
import {toast} from "react-toastify";


export function getTaskList() {
    const settings = {
        method: 'GET',
        credentials: 'include',
        cache: 'no-cache'
    };
    return ajax(setting.API_URL + '/runninglist', settings);
}

export function createTask(formData) {
    const settings = {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(formData)
    };
    return ajax(setting.API_URL + '/task', settings)
}

export function updateTask(formData) {
    const settings = {
        method: 'PUT',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(formData)
    };
    return ajax(setting.API_URL + '/task', settings)
}

export function changeTaskStatus(taskId, status, dayIndex) {
    const settings = {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
            taskId: taskId,
            status: status,
            dayIndex: dayIndex
        })
    };
    return ajax(setting.API_URL + "/task/status", settings)
}

export function changePriority(startIndex, endIndex) {
    const settings = {
        method: 'PUT',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
            startIndex: startIndex,
            endIndex: endIndex,
        })
    };
    return ajax(setting.API_URL + "/task/priority", settings)
}

export function deleteTask(taskId) {
    const settings = {
        method: 'DELETE',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        credentials: 'include'
    };
    return ajax(setting.API_URL + "/task/" + taskId, settings)
}

export function getNextTaskList(year, week) {
    const settings = {
        method: 'GET',
        credentials: 'include',
        cache: 'no-cache'
    };
    return ajax(setting.API_URL + '/archive/next?year=' + year + '&week=' + week, settings);
}

export function getPrevTaskList(year, week) {
    const settings = {
        method: 'GET',
        credentials: 'include',
        cache: 'no-cache'
    };
    return ajax(setting.API_URL + '/archive/prev?year=' + year + '&week=' + week, settings);
}

export function archive(weekId) {
    const settings = {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify({
            year: weekId.year,
            week: weekId.week
        }),
        credentials: 'include',
    };
    return ajax(setting.API_URL + '/archive', settings);
}

export function getExistingArchivesWeekIds() {
    const settings = {
        method: 'GET',
        cache: 'no-cache',
        headers: {
            'Accept': 'application/json',
        },
        credentials: 'include',
    };
    return ajax(setting.API_URL + '/archive', settings);
}

export function undo() {
    const settings = {
        method: 'DELETE',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        credentials: 'include',
    };
    return ajax(setting.API_URL + '/undo', settings);
}

export function redo() {
    const settings = {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        credentials: 'include',
    };
    return ajax(setting.API_URL + '/redo', settings);
}

export function removeTag(tag) {
    const settings = {
        method: 'DELETE',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
            tag: tag
        })
    };
    return ajax(setting.API_URL + '/tag', settings);
}

export function addTag(tag) {
    const settings = {
        method: 'PUT',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
            tag: tag
        })
    };
    return ajax(setting.API_URL + '/tag', settings);
}

export function signOut() {
    const settings = {
        method: 'GET',
        credentials: 'include',
    };
    return ajax(setting.API_URL + '/logout', settings)
}

export function getUserProfile() {
    const settings = {
        method: 'GET',
        credentials: 'include',
        cache: 'no-cache'
    };
    return ajax(setting.API_URL + '/user', settings);
}

export function saveProfile({tags, username, email}) {
    const settings = {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            email: email,
            tags: tags
        })
    };
    return ajax(setting.API_URL + '/profile', settings);
}

export function changePassword(request) {
    const settings = {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify({
            password: request.password,
            confirmPassword: request.confirmPassword
        })
    };
    return ajax(setting.API_URL + '/user/password', settings);
}

export function signIn({username, password}) {

    const settings = {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'TIMEZONE_OFFSET': new Date().getTimezoneOffset()
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    };

    const url = setting.API_URL + '/login';

    return new Promise((resolve, reject) => {
        fetch(url, settings)
            .then(parseJSON)
            .then(response => {
                if (response.status === 403) {
                    reject()
                } else {

                    if (response.json.message) {

                        const toastSettings = {
                            position: "top-right",
                            autoClose: 1500,
                            hideProgressBar: true,
                            closeOnClick: true,
                            pauseOnHover: true,
                            draggable: false
                        };

                        switch (response.json.message.type) {
                            case "ERROR":
                                toast.error(response.json.message.payload, toastSettings);
                                break;
                            default:
                                toast.info(response.json.message.payload, toastSettings);
                        }
                    }

                    resolve(response.json)
                }
            });
    });
}

export function signUp({username, email, password, confirmPassword}) {

    const settings = {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            email: email,
            password: password,
            confirmPassword: confirmPassword
        })
    };

    const url = setting.API_URL + '/user';

    return new Promise((resolve, reject) => {
        fetch(url, settings)
            .then(response => {
                switch (response.status) {
                    case 200:
                        resolve(true);
                        return response;
                    case 400:
                        return response;
                    case 502:
                        resolve(false);
                        return response;

                    default:
                        console.error("Illegal http response status: " + response.status);
                        reject(response.json)
                }
            })
            .then(parseJSON)
            .then((response) => reject(response.json));
        ;
    });
}

export function sendChangePasswordEmail({email}) {
    const settings = {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify({
            email: email
        })
    };

    const url = setting.API_URL + '/password';

    return new Promise((resolve, reject) => {
        fetch(url, settings)
            .then(response => {
                switch (response.status) {
                    case 200:
                        resolve(true);
                        return response;
                    case 400:
                        return response;
                    case 502:
                        resolve(false);
                        return response;

                    default:
                        console.error("Illegal http response status: " + response.status);
                        reject(response.json)
                }
            })
            .then(parseJSON)
            .then((response) => reject(response.json));
    });
}

/* CONVENIENCE */

/**
 * Parses the JSON returned by a network request
 *
 * @param  {object} response A response from a network request
 *
 * @return {object}          The parsed JSON, status from the response
 */
function parseJSON(response) {
    return new Promise((resolve) => response.json()
        .then((json) => resolve({
            status: response.status,
            ok: response.ok,
            json,
        })));
}

/**
 * Requests a URL, returning a promise
 *
 * @param  {string} url       The URL we want to request
 * @param  {object} [options] The options we want to pass to "fetch"
 *
 * @return {Promise}           The request promise
 */
function ajax(url, options) {

    options.headers = options.headers || {};
    options.headers['TIMEZONE_OFFSET'] = new Date().getTimezoneOffset();

    const toastSettings = {
        position: "top-right",
        autoClose: 1500,
        hideProgressBar: true,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: false
    };

    return new Promise((resolve, reject) => {
        fetch(url, options)
            .then(response => {
                switch (response.status) {
                    case 200:
                        return new Promise(() => response.json().then((json) => {

                            if (json.message) {
                                toast.info(json.message.payload, toastSettings);
                            }
                            resolve(json)
                        }));
                    case 400:
                        return new Promise(() => response.json().then((json) => {
                            if (json.message) {
                                toast.error(json.message.payload, toastSettings);
                            }
                            reject(json)
                        }));
                    case 403:
                        window.location = "/signin";
                        break;
                }
            })
            .catch(error => {
                window.location = "/error";
            })
    });
}
