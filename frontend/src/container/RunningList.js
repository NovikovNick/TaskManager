import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../store/ReduxActions";
import * as REST from "../rest/rest";

import {DragDropContext, Draggable, Droppable} from "react-beautiful-dnd";
import RunningListItem from "../component/RunningListItem";
import RunningListHeader from "../component/RunningListHeader";
import {CreateTaskModalForm, UpdateTaskModalForm} from "../component/TaskModalForm";


const getItemStyle = (isDragging, draggableStyle) => ({
    userSelect: "none",
    background: isDragging ? "white" : "none",
    ...draggableStyle
});

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
            ...props
        };

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


    toggleCreateTaskForm = () => {
        const {createTaskForm} = this.state
        createTaskForm.uiSchema.active = !createTaskForm.uiSchema.active;
        this.setState({createTaskForm: createTaskForm})
    }

    onCreateTask = () => {
        this.toggleCreateTaskForm();
        this.loadTaskList();
    }


    toggleUpdateTaskForm = (task = {}) => {

        const {updateTaskForm} = this.state
        updateTaskForm.uiSchema.active = !updateTaskForm.uiSchema.active;

        updateTaskForm.formData = {
            id: task.id || "",
            title: task.title || "",
            description: task.description || ""
        };
        this.setState({updateTaskForm: updateTaskForm})
    }

    onUpdateTask = () => {

        this.toggleUpdateTaskForm();
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


    render() {

        const {taskList, createTaskForm, updateTaskForm} = this.state;

        return (
            <div className="metalheart-running-list">

                <UpdateTaskModalForm schema={updateTaskForm}/>

                <CreateTaskModalForm schema={createTaskForm}/>

                <RunningListHeader onLoadTaskList={this.loadTaskList}
                                   onOpenCreateTaskForm={this.toggleCreateTaskForm}/>

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