import React, {Component} from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPaperPlane} from "@fortawesome/free-solid-svg-icons";


export default class TaskCreateModalForm extends Component {

    constructor(props) {
        super(props);
        this.state = {value: '', ...props};
    }

    handleEnter = (event) => {
        if (event.key === 'Enter') {
            this.handleSubmit(event);
        }
    }


    handleChange = (event) => {
        this.setState({value: event.target.value});
    }

    handleSubmit = (event) => {
        this.state.onClickHandler(this.state.value)
        event.preventDefault();
        this.setState({value: ''});
    }

    render() {

        return (
            <div id="modalLoginForm"
                 className="modal fade"
                 tabIndex="-1"
                 role="dialog"
                 aria-labelledby="myModalLabel"
                 aria-hidden="true">

                <div className="modal-dialog"
                     style={{maxWidth: "600px"}}
                     role="document">

                    <div className="modal-content">
                        <div className="modal-header text-center">
                            <h4 className="modal-title w-100 font-weight-bold">Create new task</h4>
                            <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>

                        <div className="modal-body">

                            <form>
                                <div className="form-row p-1">
                                    <div className="col-2">
                                        <label htmlFor="validationServer03">Title</label>
                                    </div>
                                    <div className="col-10">
                                        <input type="text"
                                               className="form-control"
                                               id="validationServer03"
                                               placeholder="Title"
                                               value={this.state.value}
                                               onKeyUp={this.handleEnter}
                                               onChange={this.handleChange}/>
                                    </div>
                                </div>
                                <div className="form-row p-1">
                                    <div className="col-2">
                                        <label htmlFor="validationServer01">Description</label>
                                    </div>
                                    <div className="col-10">
                                        <textarea type="text"
                                                  style={{height: "600px"}}
                                                  className="form-control"
                                                  id="validationServer01"
                                                  placeholder="Description"
                                                  value={this.state.value}
                                                  onKeyUp={this.handleEnter}
                                                  onChange={this.handleChange}/>
                                    </div>
                                </div>
                            </form>

                        </div>
                        <div className="modal-footer d-flex">
                            <button className="btn btn-info float-right"
                                    onClick={this.handleSubmit}>
                                Create
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
