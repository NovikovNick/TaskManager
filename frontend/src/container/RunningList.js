import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../store/ReduxActions";
import * as REST from "../rest/rest";

import {DragDropContext, Draggable, Droppable} from "react-beautiful-dnd";
import RunningListItem from "../component/RunningListItem";
import {CreateTaskModalForm, UpdateTaskModalForm} from "../component/TaskModalForm";

import ArchiveModalForm from "../component/ArchiveModalForm"

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faChevronLeft,
    faChevronRight,
    faPlus,
    faRedo,
    faSave,
    faSyncAlt,
    faUndo
} from "@fortawesome/free-solid-svg-icons";
import Button from "react-bootstrap/Button";
import RunningListDaysHeader from "../component/RunningListDaysHeader";
import {WithContext as ReactTags} from "react-tag-input";
import {useTranslation} from "react-i18next";

const KeyCodes = {
    comma: 188,
    enter: 13,
};

const getItemStyle = (isDragging, draggableStyle) => ({
    userSelect: "none",
    background: isDragging ? "white" : "none",
    ...draggableStyle
});

function Tags({runningList, handleDelete, handleAddition}) {

    const {t} = useTranslation();
    return (
        <div className={"running-list-tags"}>
            <ReactTags tags={runningList.selectedTags || []}
                       placeholder={t("Filter by tags")}
                       suggestions={runningList.allTags || []}
                       handleDelete={handleDelete}
                       handleAddition={handleAddition}
                       delimiters={[KeyCodes.comma, KeyCodes.enter]}/>
        </div>
    );
}

class RunningList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            createTaskForm: {
                uiSchema: {active: false},
                formData: {title: '', description: ''},
                onSubmit: REST.createTask,
                onSuccess: this.onCreateTask,
                closeForm: this.toggleCreateTaskForm
            },
            updateTaskForm: {
                uiSchema: {active: false},
                formData: {title: '', description: ''},
                onSubmit: REST.updateTask,
                onSuccess: this.onUpdateTask,
                closeForm: this.toggleUpdateTaskForm
            },
            archiveForm: {
                uiSchema: {active: false},
                formData: {weekId: {}},
                onSubmit: REST.archive,
                onSuccess: this.onArchiveTask,
                closeForm: this.toggleArchiveForm
            },
            ...props
        };

        this.loadTaskList();
    }

    ctrlQ = (e) => {
        // CTRL + Q
        if(e.keyCode===81 && e.ctrlKey) this.toggleCreateTaskForm();
    }

    ctrlZ = (e) => {
        const {runningList} = this.state;
        // CTRL + Z
        if(e.keyCode===90 && (e.ctrlKey && !e.shiftKey) &&  runningList.canUndo) this.onUndo();
    }

    ctrlShiftZ = (e) => {
        const {runningList} = this.state;
        // CTRL + SHIFT + Z
        if(e.keyCode===90 && (e.ctrlKey && e.shiftKey)  && runningList.canRedo) this.onRedo();
    }

    componentDidMount(){
        document.addEventListener('keydown', this.ctrlQ);
        document.addEventListener('keydown', this.ctrlZ);
        document.addEventListener('keydown', this.ctrlShiftZ);
    }

    componentWillUnmount(){
        document.removeEventListener('keydown', this.ctrlQ);
        document.removeEventListener('keydown', this.ctrlZ);
        document.removeEventListener('keydown', this.ctrlShiftZ);
    }

    onDragEnd = (result) => {
        // dropped outside the list
        if (!result.destination) {
            return;
        }

        const startIndex = result.source.index;
        const endIndex = result.destination.index;

        const {runningList} = this.state;

        // reorder
        const taskList = Array.from(runningList.tasks);
        const [removed] = taskList.splice(startIndex, 1);
        taskList.splice(endIndex, 0, removed);

        // save state
        this.setState({runningList: runningList});
        runningList.tasks = taskList;
        this.state.actions.setRunningList(runningList);

        // send request and update tasks to check state
        const that = this;
        REST.changePriority(startIndex, endIndex).then(that.loadTaskList)
    }

    loadTaskList = () => {

        const that = this;

        REST.getTaskList()
            .then(runningList => {
                that.setState({runningList: runningList});
                that.state.actions.setRunningList(runningList);
            });
    }

    onNext = () => {
        const {year, week} = this.state.runningList;
        const that = this;

        REST.getNextTaskList(year, week)
            .then(runningList => {
                that.setState({runningList: runningList});
                that.state.actions.setRunningList(runningList);
            });
    }

    onPrev = () => {
        const {year, week} = this.state.runningList;
        const that = this;

        REST.getPrevTaskList(year, week)
            .then(runningList => {
                that.setState({runningList: runningList});
                that.state.actions.setRunningList(runningList);
            });
    }

    onUndo = () => {
        const that = this;
        REST.undo()
            .then(runningList => {
                that.setState({runningList: runningList});
                that.state.actions.setRunningList(runningList);
            });
    }

    onRedo = () => {
        const that = this;
        REST.redo()
            .then(runningList => {
                that.setState({runningList: runningList});
                that.state.actions.setRunningList(runningList);
            });
    }

    toggleCreateTaskForm = () => {
        const {runningList, createTaskForm} = this.state
        createTaskForm.uiSchema.active = !createTaskForm.uiSchema.active;
        createTaskForm.formData = {
            ...createTaskForm.formData,
            tags: [],
            tagsSuggestion: runningList.allTags || []
        };
        this.setState({createTaskForm: createTaskForm})
    }

    onCreateTask = () => {
        this.toggleCreateTaskForm();
        this.loadTaskList();
    }


    toggleUpdateTaskForm = (task = {}) => {

        const {runningList, updateTaskForm} = this.state
        updateTaskForm.uiSchema.active = !updateTaskForm.uiSchema.active;

        updateTaskForm.formData = {
            id: task.id || "",
            title: task.title || "",
            description: task.description || "",
            tags: task.tags || [],
            tagsSuggestion: runningList.allTags || []
        };
        this.setState({updateTaskForm: updateTaskForm})
    }

    onUpdateTask = () => {

        this.toggleUpdateTaskForm();
        this.loadTaskList();
    }

    toggleArchiveForm = () => {
        const {archiveForm} = this.state
        archiveForm.uiSchema.active = !archiveForm.uiSchema.active;
        this.setState({archiveForm: archiveForm})
    }

    onArchiveTask = () => {

        this.toggleArchiveForm();
        this.loadTaskList();
    }

    handleChangeTaskTitle = (task) => {
        const that = this;
        REST.updateTask(task)
            .then(() => that.loadTaskList())
    }

    handleRemove = (task) => {
        const that = this;
        REST.deleteTask(task.id)
            .then(() => that.loadTaskList())
    }

    changeStatus = (task, status, dayIndex) => {
        const that = this;
        REST.changeTaskStatus(task.id, status, dayIndex)
            .then(() => that.loadTaskList())
    }


    handleDelete = (i) => {

        const that = this;
        const selectedTag = this.state.runningList.selectedTags[i];
        selectedTag && REST.removeTag(selectedTag.text)
            .then(() => that.loadTaskList());
    }

    handleAddition = (tag) => {
        const that = this;
        REST.addTag(tag.text)
            .then(() => that.loadTaskList());
    }


    render() {

        const {createTaskForm, updateTaskForm, archiveForm} = this.state;
        const {runningList} = this.props;

        return (
            <div className="metalheart-running-list">

                <UpdateTaskModalForm schema={updateTaskForm}/>

                <CreateTaskModalForm schema={createTaskForm}/>

                <ArchiveModalForm schema={archiveForm} />

                <div style={{'position': 'relative', 'marginTop': '40px'}}>

                    <DragDropContext onDragEnd={this.onDragEnd}>
                        <Droppable droppableId="droppable">
                            {(provided, snapshot) => (
                                <div
                                    {...provided.droppableProps}
                                    ref={provided.innerRef}
                                >
                                    {runningList.tasks.map((task, index) => (
                                        <Draggable key={task.id} draggableId={task.id + ''} index={index}>
                                            {(provided, snapshot) => (
                                                <div
                                                    ref={provided.innerRef}
                                                    {...provided.draggableProps}
                                                    {...provided.dragHandleProps}
                                                    style={getItemStyle(
                                                        snapshot.isDragging,
                                                        provided.draggableProps.style
                                                    )}
                                                >
                                                    <RunningListItem
                                                        key={task.id}
                                                        index={index}
                                                        task={task}
                                                        handleRemove={this.handleRemove}
                                                        changeStatus={this.changeStatus}
                                                        changeTaskTitle={this.handleChangeTaskTitle}
                                                        handleEdit={this.toggleUpdateTaskForm}
                                                    />
                                                </div>
                                            )}
                                        </Draggable>
                                    ))}
                                    {provided.placeholder}
                                </div>
                            )}
                        </Droppable>
                    </DragDropContext>

                    <div className={"overlay"} style={{display: runningList.editable ? 'none' : 'block'}}></div>

                </div>

                <div className="metalheart-running-list-header">

                    <RunningListDaysHeader calendar={runningList.calendar}/>

                    <div className={'running-list-title'}>

                        <div className={"running-list-tags"}>
                            <Tags
                                runningList={runningList}
                                handleDelete={this.handleDelete}
                                handleAddition={this.handleAddition}/>
                        </div>
                    </div>
                    <div className={'running-list-controls'}>

                        <Button variant="outline-light" disabled={!runningList.canUndo} onClick={this.onUndo}>
                            <FontAwesomeIcon icon={faUndo}/>
                        </Button>

                        <Button variant="outline-light" disabled={!runningList.canRedo} onClick={this.onRedo}>
                            <FontAwesomeIcon icon={faRedo}/>
                        </Button>

                        <Button variant="outline-light" disabled={!runningList.editable} onClick={this.toggleArchiveForm}>
                            <FontAwesomeIcon icon={faSave}/>
                        </Button>

                        <Button variant="outline-light" disabled={!runningList.hasNext} onClick={this.onNext}>
                            <FontAwesomeIcon icon={faChevronLeft}/>
                        </Button>

                        <Button variant="outline-light" disabled={!runningList.hasPrevious} onClick={this.onPrev}>
                            <FontAwesomeIcon icon={faChevronRight}/>
                        </Button>

                        <Button variant="outline-light" onClick={this.loadTaskList}>
                            <FontAwesomeIcon icon={faSyncAlt}/>
                        </Button>

                        <Button variant="outline-light" onClick={this.toggleCreateTaskForm}>
                            <FontAwesomeIcon icon={faPlus}/>
                        </Button>
                    </div>
                </div>
            </div>
        )
    }
}

const mapStateToProps = state => ({
    runningList: state.task.runningList
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(RunningList);