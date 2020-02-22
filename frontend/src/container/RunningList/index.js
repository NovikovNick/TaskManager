import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../../store/ReduxActions";

import {DragDropContext, Draggable, Droppable} from "react-beautiful-dnd";
import TaskItem from "../../component/runninglist/TaskItem";
import UpdateTaskModalForm from "../../component/form/UpdateTaskModalForm";

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faSyncAlt} from "@fortawesome/free-solid-svg-icons";
import Button from "react-bootstrap/Button";
import Days from "../../component/runninglist/Days";
import CreateTaskHeaderControl from "../../component/runninglist/CreateTaskHeaderControl";
import ArchiveHeaderControl from "../../component/runninglist/ArchiveHeaderControl";
import UndoHeaderControl from "../../component/runninglist/UndoHeaderControl";
import RedoHeaderControl from "../../component/runninglist/RedoHeaderControl";
import NextArchiveHeaderControl from "../../component/runninglist/NextArchiveHeaderControl";
import PrevArchiveHeaderControl from "../../component/runninglist/PrevArchiveHeaderControl";
import TagsHeader from "../../component/runninglist/TagsHeader";
import PropTypes from "prop-types";
import "./styles.scss";

const getItemStyle = (isDragging, draggableStyle) => ({
    userSelect: "none",
    background: isDragging ? "white" : "none",
    ...draggableStyle
});

function RunningList({runningList, actions}) {


    const [task, setTask] = useState({});
    const [isUpdateTaskModalActive, setUpdateTaskModalActive] = useState(false);
    const loadTaskList = () => {
        actions.getTaskList();
    }

    useEffect(() => loadTaskList(), []);

    const toggleUpdateTaskForm = (task = {}) => {
        setTask(task);
        setUpdateTaskModalActive(true);
    }

    const onDragEnd = (result) => {
        // dropped outside the list
        if (!result.destination) {
            return;
        }

        const startIndex = result.source.index;
        const endIndex = result.destination.index;

        // reorder
        const taskList = Array.from(runningList.tasks);
        const [removed] = taskList.splice(startIndex, 1);
        taskList.splice(endIndex, 0, removed);

        // save state
        //this.setState({runningList: runningList});
        runningList.tasks = taskList;
        actions.setRunningList(runningList);

        // send request and update tasks to check state
        actions.changePriority(startIndex, endIndex);
    }

    return (
        <div className="metalheart-runninglist">

            <div className={'metalheart-runninglist-tasks'}>

                <DragDropContext onDragEnd={onDragEnd}>
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
                                                <TaskItem
                                                    key={task.id}
                                                    task={task}
                                                    handleEdit={toggleUpdateTaskForm}
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

            <div className="metalheart-runninglist-header">

                <Days/>

                <TagsHeader/>

                <div className={'running-list-controls'}>

                    <UndoHeaderControl/>

                    <RedoHeaderControl/>

                    <ArchiveHeaderControl/>

                    <NextArchiveHeaderControl/>

                    <PrevArchiveHeaderControl/>

                    <Button variant="outline-light" onClick={loadTaskList}>
                        <FontAwesomeIcon icon={faSyncAlt}/>
                    </Button>

                    <CreateTaskHeaderControl/>
                </div>
            </div>

            <UpdateTaskModalForm isActive={isUpdateTaskModalActive}
                                 toggle={setUpdateTaskModalActive}
                                 task={task}/>
        </div>
    )
}

RunningList.propTypes = {
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
export default connect(mapStateToProps, mapDispatchToProps)(RunningList);