import React, {Component} from 'react';

import {WithContext as ReactTags} from "react-tag-input";
import {useTranslation} from "react-i18next";
import KeyCodes from "../../KeyCodes";

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

class Tags extends Component {

    render() {
        const {tags, suggestions, className, placeholder, handleDelete, handleAddition} = this.props;
        return (
            <div className={className}>
                <ReactTags
                    tags={tags}
                    placeholder={placeholder}
                    suggestions={suggestions}
                    handleDelete={handleDelete}
                    handleAddition={handleAddition}

                    handleDrag={() => {}}
                    handleTagClick={() => {}}
                    delimiters={[KeyCodes.comma, KeyCodes.enter]}
                />
            </div>
        );
    }
}