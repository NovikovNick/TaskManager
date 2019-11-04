import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../store/ReduxActions";
import * as REST from "../rest/rest";

import {DragDropContext, Draggable, Droppable} from "react-beautiful-dnd";
import TaskModalForm from "../component/TaskModalForm";
import RunningListItem from "../component/RunningListItem";
import RunningListHeader from "../component/RunningListHeader";


const getItemStyle = (isDragging, draggableStyle) => ({
    userSelect: "none",
    background: isDragging ? "white" : "none",
    ...draggableStyle
});

class RunningList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            taskFormData: {},
            ...props
        };

        this.createTaskForm = React.createRef();
        this.updateTaskForm = React.createRef();

        this.loadTaskList();
    }

    onDragEnd = (result) => {
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

    showCreateTaskForm = () => {
        this.createTaskForm.current.show();
    }

    createTask = (value) => {

        const that = this;
        REST.createTask(value.formData)
            .then(() => {
                that.createTaskForm.current.hide()
                that.loadTaskList();
            })
            .catch(error => that.createTaskForm.current.error(error))
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
        this.updateTaskForm.current.show();
    }

    updateTask = (value) => {

        const that = this;
        REST.updateTask(value.formData)
            .then(() => {
                that.updateTaskForm.current.hide();
                this.setState({taskFormData: {}});
                that.loadTaskList();
            })
            .catch(error => that.updateTaskForm.current.error(error))
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

    render() {

        const {taskList, taskFormData} = this.state;

        return (
            <div className="metalheart-running-list">

                <TaskModalForm ref={this.createTaskForm}
                               id={"createTaskModalForm"}
                               onSubmit={this.createTask}/>

                <TaskModalForm ref={this.updateTaskForm}
                               id={"updateTaskModalForm"}
                               formData={taskFormData}
                               onSubmit={this.updateTask}/>

                <RunningListHeader onLoadTaskList={this.loadTaskList}
                                   onOpenCreateTaskForm={this.showCreateTaskForm}/>

                <DragDropContext onDragEnd={this.onDragEnd}>
                    <Droppable droppableId="droppable">
                        {(provided, snapshot) => (
                            <div
                                {...provided.droppableProps}
                                ref={provided.innerRef}
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
                                                <RunningListItem
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