import React, {useEffect} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import Button from "react-bootstrap/Button";
import CreateTaskModalForm from "../form/CreateTaskModalForm";
import useModal from "../../hook/useModal";
import KeyCodes from "../../KeyCodes";


export default function CreateTaskHeaderControl() {

    const {isActive, toggle} = useModal();

    useEffect(() => {

        const ctrlQ = (e) => {
            if (e.keyCode === KeyCodes.Q && e.ctrlKey) {
                toggle()
            };
        }

        document.addEventListener('keydown', ctrlQ);

        return () => {
            document.removeEventListener('keydown', ctrlQ);
        }
    });

    return (
        <span>
            <Button variant="outline-light" onClick={toggle}>
                <FontAwesomeIcon icon={faPlus}/>
            </Button>
            <CreateTaskModalForm isActive={isActive} toggle={toggle}/>
        </span>
    );
}
