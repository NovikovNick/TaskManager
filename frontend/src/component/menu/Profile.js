import React from 'react';
import {useTranslation} from "react-i18next";
import {Dropdown} from "react-bootstrap";
import useModal from "../../hook/useModal";
import ProfileModalForm from "../form/ProfileModalForm";


export default function Profile() {

    const {isActive, toggle} = useModal();
    const {t} = useTranslation();

    return (
        <span>
            <Dropdown.Item onClick={toggle}>{t("Profile")}</Dropdown.Item>
            <ProfileModalForm
                isActive={isActive}
                toggle={toggle}
            />
        </span>
    );
}
