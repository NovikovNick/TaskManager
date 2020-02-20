import React, {useEffect} from "react";
import {toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';


export default function PopupMessages() {

    useEffect(() => toast.configure())

    return (
        <ToastContainer
            autoClose={1500}
            hideProgressBar={true}/>
    );
}