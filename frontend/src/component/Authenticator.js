import React, {useState} from "react";
import setting from "../config";
import {useHistory} from "react-router-dom";

export default function Authenticator() {

    const [requestedLogin, setRequestedLogin] = useState(false);
    const history = useHistory();

    if (!requestedLogin) {
        const settings = {
            method: 'GET',
            credentials: 'include',
            cache: 'no-cache'
        };
        fetch(setting.API_URL + '/user', settings).then(response => {
            if (response.status === 403) {
                history.push("/signin");
            }
            return response;

        });
        setRequestedLogin(true);
    }
    return <div/>
}
