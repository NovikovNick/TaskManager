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
    return rest('/taskmanager/task/list', settings)
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
    return rest('/taskmanager/task', settings)
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
    return rest('/taskmanager/task', settings)
}

export function removeTask(taskId) {
    const settings = {
        method: 'DELETE',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
            taskId: taskId
        })
    };
    return rest('/taskmanager/task', settings)
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
    return rest("taskmanager/task/status", settings)
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
    return rest("taskmanager/task/priority", settings)
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
    return rest("taskmanager/task/" + taskId, settings)
}