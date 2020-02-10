import React from 'react';
import {WithContext as ReactTags} from "react-tag-input";
import {useTranslation} from "react-i18next";

export function HeaderTags(props) {
    const {t} = useTranslation();
    return (<Tags className={"running-list-header-tag"}
                  placeholder={t("Filter by tags")}
                  {...props} > </Tags>);
}

export function FormTags(props) {
    return (<Tags className={"running-list-form-tag"}
                  {...props} > </Tags>);
}

export default function Tags({className, tags, placeholder, suggestions, handleDelete, handleAddition}) {
    return (
        <div className={className}>
            <ReactTags tags={tags}
                       placeholder={placeholder}
                       suggestions={suggestions}
                       handleDelete={handleDelete}
                       handleAddition={handleAddition}/>
        </div>);
}