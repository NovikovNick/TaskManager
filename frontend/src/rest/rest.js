import setting from "../config"

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
function rest(url, options) {

    return new Promise((resolve, reject) => {
        fetch(url, options)
            .then(response => {

                if (response.status === 403) {
                    window.location = "/signin";
                    console.log("Unauthorised ", response)
                }

                return response;
            })
            .then(parseJSON)
            .then((response) => {

                if (response.ok) {
                    return resolve(response.json);
                }
                // extract the error from the server's json
                return reject(response.json);
            })
            .catch((error) => reject(error));
    });
}

export function getTaskList() {
    const settings = {
        method: 'GET',
        credentials: 'include',
        cache: 'no-cache'
    };
    return rest(setting.API_URL + '/taskmanager/runninglist', settings)
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
    return rest(setting.API_URL + '/taskmanager/task', settings)
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
    return rest(setting.API_URL + '/taskmanager/task', settings)
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
    return rest(setting.API_URL + "/taskmanager/task/status", settings)
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
    return rest(setting.API_URL + "/taskmanager/task/priority", settings)
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
    return rest(setting.API_URL + "/taskmanager/task/" + taskId, settings)
}

export function getNextTaskList(year, week) {
    const settings = {
        method: 'GET',
        credentials: 'include',
        cache: 'no-cache'
    };
    return rest(setting.API_URL + '/taskmanager/runninglist/archive/next?year=' + year + '&week=' + week, settings);
}

export function getPrevTaskList(year, week) {
    const settings = {
        method: 'GET',
        credentials: 'include',
        cache: 'no-cache'
    };
    return rest(setting.API_URL + '/taskmanager/runninglist/archive/prev?year=' + year + '&week=' + week, settings);
}

export function archive() {
    const settings = {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        credentials: 'include',
    };
    return rest(setting.API_URL + '/taskmanager/runninglist/archive', settings);
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
    return rest(setting.API_URL + '/taskmanager/runninglist', settings);
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
    return rest(setting.API_URL + '/taskmanager/runninglist', settings);
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
    return rest(setting.API_URL + '/taskmanager/tag', settings);
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
    return rest(setting.API_URL + '/taskmanager/tag', settings);
}

export function signOut(){
    const settings = {
        method: 'GET',
        credentials: 'include',
    };
    return rest(setting.API_URL + '/auth/signout', settings)
}

export function signIn({username, password}) {

    const settings = {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    };
    return rest(setting.API_URL + '/auth/signin', settings);
}

export function getUserProfile() {
    const settings = {
        method: 'GET',
        credentials: 'include',
        cache: 'no-cache'
    };
    return rest(setting.API_URL +'/user', settings);
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
    return rest(setting.API_URL + '/user', settings);
}
