import React from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Service from "../../service/service";
import * as Store from "../../store/ReduxActions";
import {useTranslation} from "react-i18next";
import {Tags} from "../vendor/Tags";


function TagsHeader({actions, runningList}) {

    const {t} = useTranslation();

    const handleDelete = (i) => {

        const selectedTag = runningList.selectedTags[i];
        selectedTag && Service.removeTag(selectedTag.text).then(actions.setRunningList);
    }

    const handleAddition = (tag) => {
        Service.addTag(tag.text).then(actions.setRunningList);
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

const mapStateToProps = state => ({
    runningList: state.task.runningList
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(TagsHeader);