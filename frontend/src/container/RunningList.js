import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../store/ReduxActions";
import * as REST from "../rest/rest";
import $ from 'jquery';
import {faDownload, faPen, faPlus, faTrash} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

import {DragDropContext, Draggable, Droppable} from "react-beautiful-dnd";
import TaskModalForm from "../component/TaskModalForm";

// a little function to help us with reordering the result
const reorder = (list, startIndex, endIndex) => {
    const result = Array.from(list);
    const [removed] = result.splice(startIndex, 1);
    result.splice(endIndex, 0, removed);

    return result;
};

const getItemStyle = (isDragging, draggableStyle) => ({
    userSelect: "none",
    background: isDragging ? "white" : "none",
    ...draggableStyle
});

const getListStyle = isDraggingOver => ({
    //background: isDraggingOver ? "lightblue" : "none",
    //padding: grid,
    //width: 250
});

class TaskItem extends Component {

    constructor(props) {
        super(props);
        this.state = {...props};
    }

    handleRemove = () => {
        const {task, handleRemove, index} = this.props;
        handleRemove(task, index)
    }

    changeStatus = (e, status, dayIndex) => {
        const {task, changeStatus, index} = this.props;
        changeStatus(task, status, dayIndex)
    }

    changeText = (e) => {
        const {task, changeTaskTitle} = this.props;
        task.title = e.target.value;

        changeTaskTitle(task)
    }

    handleEdit = (e) => {
        const {task, handleEdit} = this.props;
        task.content = e.target.value;

        handleEdit(task)
    }

    render() {

        const {task} = this.props;

        const key = task.id

        return (
            <div className="running-list-row">

                {
                    task.status.map((state, dayIndex) =>
                        <div key={key + '_' + dayIndex} index={dayIndex} className={"running-list-cell " + state}>
                            <div className="dropdown show line-through">
                                <div role="button"
                                     id={key + "_dropdownMenuLink_" + dayIndex} data-toggle="dropdown"
                                     aria-haspopup="true"
                                     aria-expanded="false">
                                    &nbsp;
                                </div>

                                <div className="dropdown-menu" aria-labelledby="dropdownMenuLink">
                                    <button className="dropdown-item"
                                            onClick={(e) => this.changeStatus(e, "NONE", dayIndex)}>NONE
                                    </button>
                                    <button className="dropdown-item"
                                            onClick={(e) => this.changeStatus(e, "TO_DO", dayIndex)}>TO_DO
                                    </button>
                                    <button className="dropdown-item"
                                            onClick={(e) => this.changeStatus(e, "IN_PROGRESS", dayIndex)}>IN_PROGRESS
                                    </button>
                                    <button className="dropdown-item"
                                            onClick={(e) => this.changeStatus(e, "CANCELED", dayIndex)}>CANCELED
                                    </button>
                                    <button className="dropdown-item"
                                            onClick={(e) => this.changeStatus(e, "DONE", dayIndex)}>DONE
                                    </button>
                                </div>
                            </div>
                        </div>
                    )
                }

                <div className="running-list-task">
                         <span>
                             <span className="pl-1"><FontAwesomeIcon onClick={this.handleEdit} icon={faPen}/></span>
                             <span className="pl-1"><FontAwesomeIcon onClick={this.handleRemove} icon={faTrash}/></span>
                        </span>
                    <input type={"text"} onChange={this.changeText} value={task.title}></input>
                </div>
            </div>
        );
    }
}


class RunningList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            taskFormData: {},
            ...props
        };
        this.onDragEnd = this.onDragEnd.bind(this);
        this.loadTaskList();
    }

    onDragEnd(result) {
        // dropped outside the list
        if (!result.destination) {
            return;
        }

        const taskList = Array.from(this.state.taskList);
        const [removed] = taskList.splice(result.source.index, 1);
        taskList.splice(result.destination.index, 0, removed);


        const that = this;
        that.setTaskList(taskList);
        REST.changePriority(result.source.index, result.destination.index)
            .then((taskList) => that.setTaskList(taskList))
    }

    setTaskList = (taskList) => {
        this.setState({taskList: taskList});
        this.state.actions.setTaskList(taskList);
    }

    loadTaskList = () => {

        const that = this;

        REST.getTaskList()
            .then(taskList => {
                that.setTaskList(taskList);
            });
    }

    createTask = (value) => {

        const that = this;
        REST.createTask(value.formData).then(() => {
            $('#createTaskModalForm').modal('hide');
            that.loadTaskList();
        })
    }


    handleEdit = (task) => {
        console.log(task)
        this.setState({
            taskFormData: {
                id: task.id,
                title: task.title,
                description: task.description || ""
            }
        });
        $('#updateTaskModalForm').modal('show');
    }

    updateTask = (value) => {

        const that = this;
        REST.updateTask(value.formData).then(() => {
            $('#updateTaskModalForm').modal('hide');
            this.setState({taskFormData: {}});
            that.loadTaskList();
        })

    }
    handleChangeTaskTitle = (task) => {
        const that = this;
        REST.updateTask(task)
            .then(() => that.loadTaskList())
    }

    handleRemove = (task, index) => {
        const that = this;
        REST.deleteTask(task.id)
            .then(() => that.loadTaskList())
    }

    changeStatus = (task, status, dayIndex) => {
        const that = this;
        REST.changeTaskStatus(task.id, status, dayIndex)
            .then(() => that.loadTaskList())
    }


    render() {

        const {taskList, taskFormData} = this.state;

        return (
            <div className="metalheart-running-list">

                <TaskModalForm id={"createTaskModalForm"} onSubmit={this.createTask}/>
                <TaskModalForm id={"updateTaskModalForm"} formData={taskFormData} onSubmit={this.updateTask}/>

                <div className="metalheart-running-list-header text-center">
                    <div className="row">

                        <div className="col-9">
                            <h2>Task Manager</h2>
                        </div>

                        <div className="col-3">

                            <button
                                className="btn btn-default"
                                onClick={this.loadTaskList}>
                                <FontAwesomeIcon icon={faDownload}/>
                            </button>

                            <button
                                className="btn btn-default"
                                data-toggle="modal"
                                data-target="#createTaskModalForm">
                                <FontAwesomeIcon icon={faPlus}/>
                            </button>

                        </div>
                    </div>
                </div>

                <div className="running-list-row">
                    <div className={"running-list-cell "}>
                        <div>пн</div>
                    </div>
                    <div className={"running-list-cell "}>
                        <div>вт</div>
                    </div>
                    <div className={"running-list-cell "}>
                        <div>ср</div>
                    </div>
                    <div className={"running-list-cell "}>
                        <div>чт</div>
                    </div>
                    <div className={"running-list-cell "}>
                        <div>пт</div>
                    </div>
                    <div className={"running-list-cell "}>
                        <div>сб</div>
                    </div>
                    <div className={"running-list-cell "}>
                        <div>вс</div>
                    </div>
                    <div className="running-list-task">
                        &nbsp;
                    </div>
                </div>


                <DragDropContext onDragEnd={this.onDragEnd}>
                    <Droppable droppableId="droppable">
                        {(provided, snapshot) => (
                            <div
                                {...provided.droppableProps}
                                ref={provided.innerRef}
                                style={getListStyle(snapshot.isDraggingOver)}
                            >
                                {taskList.map((task, index) => (
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
                                                    index={index}
                                                    task={task}
                                                    handleRemove={this.handleRemove}
                                                    changeStatus={this.changeStatus}
                                                    changeTaskTitle={this.handleChangeTaskTitle}
                                                    handleEdit={this.handleEdit}
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

            </div>
        )
    }
}

const mapStateToProps = state => ({
    taskList: state.task.taskList
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(RunningList);