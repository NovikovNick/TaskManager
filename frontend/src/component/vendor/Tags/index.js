import React from 'react';

import {WithContext as ReactTags} from "react-tag-input";
import KeyCodes from "../../../KeyCodes";
import "./styles.scss"

export function FormTags(props) {
    return (<Tags className={"running-list-form-tag"}
                   {...props} > </Tags>);
}

export function Tags({tags, suggestions, className, placeholder, handleDelete, handleAddition}) {

    return (
        <div className={className}>
            <ReactTags
                tags={tags}
                placeholder={placeholder}
                suggestions={suggestions}
                handleDelete={handleDelete}
                handleAddition={handleAddition}

                handleDrag={() => {
                }}
                handleTagClick={() => {
                }}
                delimiters={[KeyCodes.comma, KeyCodes.enter]}
            />
        </div>
    );
}