import React from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Store from "../../store/ReduxActions";
import {useTranslation} from "react-i18next";
import {Tags} from "../vendor/Tags/index";
import PropTypes from "prop-types";


function TagsHeader({actions, runningList}) {

    const {t} = useTranslation();

    const handleDelete = (i) => {

        const selectedTag = runningList.selectedTags[i];
        selectedTag && actions.unselectTag(selectedTag.text);
    }

    const handleAddition = (tag) => {
        actions.selectTag(tag.text);
    }

    return (
        <div className={'running-list-title'}>
            <div className={"running-list-tags"}>
                <Tags
                    className={"running-list-header-tag"}
                    placeholder={t("Filter by tags")}
                    tags={runningList.selectedTags || []}
                    suggestions={runningList.allTags || []}
                    handleDelete={handleDelete}
                    handleAddition={handleAddition}/>
            </div>
        </div>
    );
}

TagsHeader.propTypes = {
    actions: PropTypes.object.isRequired,
    runningList: PropTypes.shape({
        calendar: PropTypes.object.isRequired,
        tasks: PropTypes.array.isRequired,
        selectedTags: PropTypes.array.isRequired,
        allTags: PropTypes.array.isRequired
    })
};

const mapStateToProps = state => ({
    runningList: state.task.runningList
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(TagsHeader);